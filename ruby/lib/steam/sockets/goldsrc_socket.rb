# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "byte_buffer"
require "steam/packets/steam_packet_factory"
require "steam/packets/rcon/rcon_goldsrc_request"
require "steam/sockets/steam_socket"

# The SourceSocket class is a sub class of SteamSocket respecting the
# specifications of the Source query protocol.
class GoldSrcSocket < SteamSocket

  # Reads a packet from the channel. The Source query protocol specifies a
  # maximum packet size of 1400 byte. Greater packets will be split over several
  # UDP packets. This method reassembles split packets into single packet
  # objects.
  def get_reply
    bytes_read = self.receive_packet 1400
    
    if @buffer.get_long == -2
      split_packets = Array.new
      begin
        # Parsing of split packet headers
        request_id = @buffer.get_long
        packet_number_and_count = @buffer.get_byte.to_i
        packet_count = packet_number_and_count & 0xF
        packet_number = (packet_number_and_count >> 4) + 1

        # Omit additional header on the first packet
        if packet_number == 1
          @buffer.get_long
        end
        
        # Caching of split packet Data
        split_packets[packet_number - 1] = @buffer.get
        
        warn "Received packet #{packet_number} of #{packet_count} for request ##{request_id}"
        
        # Receiving the next packet
        bytes_read = self.receive_packet
      end while bytes_read > 0 && @buffer.get_long == -2
      
      packet = SteamPacketFactory.get_packet_from_data(split_packets.join(""))
        
    else
       
      packet = SteamPacketFactory.get_packet_from_data(@buffer.get)
      
    end
    
    warn "Got reply of type \"#{packet.class.to_s}\"."
    
    return packet
  end
  
  def rcon_exec(password, command)
    if @rcon_challenge.nil?
      self.rcon_get_challenge
    end
    
    self.rcon_send "rcon #{@rcon_challenge} #{password} #{command}"
    response = self.get_reply.get_response
    
    if response.strip == "Bad rcon_password." or response.strip == "You have been banned from this server."
      raise RCONNoAuthException.new
    end
    
    begin
      self.rcon_send "rcon #{@rcon_challenge} #{password}"
      response_part = self.get_reply.get_response
      response << response_part
    end while response_part != "\0\0"
    
    return response
  end
  
  def rcon_get_challenge
    self.rcon_send "challenge rcon"
    response = self.get_reply.get_response
    
    if response.strip == "You have been banned from this server."
      raise RCONNoAuthException.new;
    end
    
    @rcon_challenge = @buffer.array[18..28].to_i
  end
  
  def rcon_send(command)
    self.send RCONGoldSrcRequest.new(command)
  end
  
end
