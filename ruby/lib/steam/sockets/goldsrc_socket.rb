# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'stringio_additions'
require 'exceptions/rcon_ban_exception'
require 'exceptions/rcon_no_auth_exception'
require 'steam/packets/steam_packet_factory'
require 'steam/packets/rcon/rcon_goldsrc_request'
require 'steam/sockets/steam_socket'

# The SourceSocket class is a sub class of SteamSocket respecting the
# specifications of the Source query protocol.
class GoldSrcSocket

  include SteamSocket

  def initialize(ipaddress, port_number = 27015, is_hltv = false)
    super ipaddress, port_number
    @is_hltv = is_hltv
  end

  # Reads a packet from the channel. The Source query protocol specifies a
  # maximum packet size of 1400 byte. Greater packets will be split over several
  # UDP packets. This method reassembles split packets into single packet
  # objects.
  def reply
    bytes_read = receive_packet 1400

    if @buffer.long == 0xFFFFFFFE
      split_packets = []
      begin
        # Parsing of split packet headers
        request_id = @buffer.long
        packet_number_and_count = @buffer.byte
        packet_count = packet_number_and_count & 0xF
        packet_number = (packet_number_and_count >> 4) + 1

        # Caching of split packet data
        split_packets[packet_number - 1] = @buffer.get

        puts "Received packet #{packet_number} of #{packet_count} for request ##{request_id}" if $DEBUG

        # Receiving the next packet
        if split_packets.size < packet_count
          begin
            bytes_read = receive_packet
          rescue TimeoutException
            bytes_read = 0
          end
        else
          bytes_read = 0
        end
      end while bytes_read > 0 && @buffer.long == 0xFFFFFFFE

      packet = SteamPacketFactory.reassemble_packet(split_packets)
    else
      packet = SteamPacketFactory.packet_from_data(@buffer.get)
    end

    puts "Got reply of type \"#{packet.class.to_s}\"." if $DEBUG

    packet
  end

  def rcon_exec(password, command)
    rcon_challenge if @rcon_challenge.nil? or @is_hltv

    rcon_send "rcon #{@rcon_challenge} #{password} #{command}"
    if @is_hltv
      begin
        response = reply.response
      rescue TimeoutException
        response = ''
      end
    else
      response = reply.response
    end

    if response.strip == 'Bad rcon_password.'
      raise RCONBanException
    elsif response.strip == 'You have been banned from this server.'
      raise RCONNoAuthException
    end

    begin
      loop do
        response_part = reply.response
        response << response_part
      end
    rescue TimeoutException
    end

    response
  end

  def rcon_challenge
    rcon_send 'challenge rcon'
    response = reply.response.strip

    raise RCONNoAuthException if response == 'You have been banned from this server.'

    @rcon_challenge = response[14..-1]
  end

  def rcon_send(command)
    send RCONGoldSrcRequest.new(command)
  end

end
