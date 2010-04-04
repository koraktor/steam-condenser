# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

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
    is_compressed = false

    if @buffer.get_long == 0xFFFFFFFE
      split_packets = Array.new
      begin
        # Parsing of split packet headers
        request_id = @buffer.get_long
        is_compressed = ((request_id & 0x80000000) != 0)
        packet_count = @buffer.get_byte.to_i
        packet_number = @buffer.get_byte.to_i + 1
        
        if is_compressed
          split_size = @buffer.get_long
          packet_checksum = @buffer.get_long
        else
          split_size = @buffer.get_short
        end

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
      
      if is_compressed
        packet = SteamPacketFactory.reassemble_packet(split_packets, true, packet_checksum)
      else
        packet = SteamPacketFactory.reassemble_packet(split_packets)
      end
    else
      packet = SteamPacketFactory.get_packet_from_data(@buffer.get)
    end

    if is_compressed
      puts "Got compressed reply of type \"#{packet.class.to_s}\"." if $DEBUG
    else
      puts "Got reply of type \"#{packet.class.to_s}\"." if $DEBUG
    end
    
    return packet
  end
  
end
