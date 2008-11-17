# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "byte_buffer"
require "steam/sockets/steam_socket"

# The SourceSocket class is a sub class of SteamSocket respecting the
# specifications of the Source query protocol.
class SourceSocket < SteamSocket

  # Reads a packet from the channel. The Source query protocol specifies a
  # maximum packet size of 1400 byte. Greater packets will be split over several
  # UDP packets. This method reassembles split packets into single packet
  # objects.
  def get_reply
    bytes_read = self.receive_packet 1400

    if @buffer.get_long == -2
      split_packets = Array.new
      begin
        request_id = @buffer.get_long
        packet_count = @buffer.get_byte.to_i
        packet_number = @buffer.get_byte.to_i + 1
        split_size = @buffer.get_short
        if packet_number == 1
          @buffer.get_long
        end
        split_packets[packet_number] = @buffer.get
        
        warn "Received packet #{packet_number} of #{packet_count} for request ##{request_id}"
        
        bytes_read = self.receive_packet
      end while bytes_read > 0 && @buffer.get_long == -2
      
      packet = SteamPacketFactory.get_packet_from_data(split_packets.join(""))
    else
      packet = SteamPacketFactory.get_packet_from_data(@buffer.get)
    end
    
    warn "Got reply of type \"#{packet.class.to_s}\"."
    
    return packet
  end
  
end
