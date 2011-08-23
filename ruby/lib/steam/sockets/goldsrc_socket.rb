# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'core_ext/stringio'
require 'errors/rcon_ban_error'
require 'errors/rcon_no_auth_error'
require 'errors/timeout_error'
require 'steam/packets/steam_packet_factory'
require 'steam/packets/rcon/rcon_goldsrc_request'
require 'steam/sockets/steam_socket'

# This class represents a socket used to communicate with game servers based on
# the GoldSrc engine (e.g. Half-Life, Counter-Strike)
#
# @author Sebastian Staudt
class GoldSrcSocket

  include SteamSocket

  # Creates a new socket to communicate with the server on the given IP address
  # and port
  #
  # @param [String] ipaddress Either the IP address or the DNS name of the
  #        server
  # @param [Fixnum] port_number The port the server is listening on
  # @param [Boolean] is_hltv `true` if the target server is a HTLV instance.
  #        HLTV behaves slightly different for RCON commands, this flag
  #        increases compatibility.
  def initialize(ipaddress, port_number = 27015, is_hltv = false)
    super ipaddress, port_number

    @is_hltv = is_hltv
  end

  # Reads a packet from the socket
  #
  # The Source query protocol specifies a maximum packet size of 1,400 bytes.
  # Bigger packets will be split over several UDP packets. This method
  # reassembles split packets into single packet objects.
  #
  # @return [SteamPacket] The packet replied from the server
  def reply
    receive_packet 1400

    if @buffer.long == 0xFFFFFFFE
      split_packets = []
      begin
        request_id = @buffer.long
        packet_number_and_count = @buffer.byte
        packet_count = packet_number_and_count & 0xF
        packet_number = (packet_number_and_count >> 4) + 1

        split_packets[packet_number - 1] = @buffer.get

        puts "Received packet #{packet_number} of #{packet_count} for request ##{request_id}" if $DEBUG

        if split_packets.size < packet_count
          begin
            bytes_read = receive_packet
          rescue SteamCondenser::TimeoutError
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

  # Executes the given command on the server via RCON
  #
  # @param [String] password The password to authenticate with the server
  # @param [String] command The command to execute on the server
  # @raise [RCONBanError] if the IP of the local machine has been banned on the
  #        game server
  # @raise [RCONNoAuthError] if the password is incorrect
  # @return [RCONGoldSrcResponse] The response replied by the server
  # @see #rcon_challenge
  # @see #rcon_send
  def rcon_exec(password, command)
    rcon_challenge if @rcon_challenge.nil? || @is_hltv

    rcon_send "rcon #{@rcon_challenge} #{password} #{command}"
    rcon_send "rcon #{@rcon_challenge} #{password}"
    if @is_hltv
      begin
        response = reply.response
      rescue SteamCondenser::TimeoutError
        response = ''
      end
    else
      response = reply.response
    end

    if response.strip == 'Bad rcon_password.'
      raise RCONNoAuthError
    elsif response.strip == 'You have been banned from this server.'
      raise RCONBanError
    end

    begin
      response_part = reply.response
      response << response_part
    end while response_part.size > 0

    response
  end

  # Requests a challenge number from the server to be used for further requests
  #
  # @raise [RCONBanError] if the IP of the local machine has been banned on the
  #        game server
  # @see #rcon_send
  def rcon_challenge
    rcon_send 'challenge rcon'
    response = reply.response.strip

    if response.strip == 'You have been banned from this server.'
      raise RCONBanError
    end

    @rcon_challenge = response[14..-1]
  end

  # Wraps the given command in a RCON request packet and send it to the server
  #
  # @param [String] command The RCON command to send to the server
  def rcon_send(command)
    send RCONGoldSrcRequest.new(command)
  end

end
