# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "byte_buffer"
require "steam/packets/steam_packet_factory"
require "steam/packets/rcon/rcon_goldsrc_request"
require "steam/sockets/steam_socket"

# The SourceSocket class is a sub class of SteamSocket respecting the
# specifications of the Source query protocol.
class GoldSrcSocket < SteamSocket

  def initialize(ipaddress, port_number = 27015, is_hltv = false)
    super ipaddress, port_number
    @is_hltv = is_hltv
  end

  # Reads a packet from the channel. The Source query protocol specifies a
  # maximum packet size of 1400 byte. Greater packets will be split over several
  # UDP packets. This method reassembles split packets into single packet
  # objects.
  def get_reply
    bytes_read = self.receive_packet 1400
    
    if @buffer.get_long == 0xFFFFFFFE
      split_packets = Array.new
      begin
        # Parsing of split packet headers
        request_id = @buffer.get_long
        packet_number_and_count = @buffer.get_byte.to_i
        packet_count = packet_number_and_count & 0xF
        packet_number = (packet_number_and_count >> 4) + 1
        
        # Caching of split packet data
        split_packets[packet_number - 1] = @buffer.get

        puts "Received packet #{packet_number} of #{packet_count} for request ##{request_id}" if $DEBUG

        # Receiving the next packet
        if split_packets.size < packet_count
          begin
            bytes_read = self.receive_packet
          rescue TimeoutException
            bytes_read = 0
          end
        else
          bytes_read = 0
        end
      end while bytes_read > 0 && @buffer.get_long == 0xFFFFFFFE
      
      packet = SteamPacketFactory.reassemble_packet(split_packets)
        
    else
      packet = SteamPacketFactory.get_packet_from_data(@buffer.get)
    end

    puts "Got reply of type \"#{packet.class.to_s}\"." if $DEBUG

    return packet
  end
  
  def rcon_exec(password, command)
    self.rcon_get_challenge if @rcon_challenge.nil? or @is_hltv
    
    self.rcon_send "rcon #{@rcon_challenge} #{password} #{command}"
    if @is_hltv
      begin
        response = self.get_reply.get_response
      rescue TimeoutException
        response = ""
      end
    else
      response = self.get_reply.get_response
    end
    
    if response.strip == "Bad rcon_password." or response.strip == "You have been banned from this server."
      raise RCONNoAuthException.new
    end
    
    begin
      while true
        response_part = self.get_reply.get_response
        response << response_part
      end
    rescue TimeoutException
    end
    
    return response
  end
  
  def rcon_get_challenge
    self.rcon_send "challenge rcon"
    response = self.get_reply.get_response.strip
    
    if response == "You have been banned from this server."
      raise RCONNoAuthException.new;
    end
    
    @rcon_challenge = response[14..-1]
  end
  
  def rcon_send(command)
    self.send RCONGoldSrcRequest.new(command)
  end
  
end
