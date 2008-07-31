autoload "DatagramChannel", "datagram_channel"

# The SteamSocket class is a sub class of CondenserSocket respecting the
# specifications of the Source query protocol.
class SteamSocket

  def initialize(ip_address, port_number)
    @channel = DatagramChannel.open
    @channel.connect ip_address.to_s, port_number
    @channel.configure_blocking false
  end

  # Reads a packet from the channel. The Source query protocol specifies a
  # maximum packet size of 1400 byte. Greater packets will be split over several
  # UDP packets. This method reassembles split packets into single packet
  # objects.
  def get_reply
    if select([@channel.socket], nil, nil, 1) == nil
      raise IOError.new
    end
    
    @buffer = ByteBuffer.allocate 1400
    bytes_read = @channel.read @buffer
    @buffer.rewind
    @buffer.limit bytes_read
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
        
        debug("Received packet #{packet_number} of #{packet_count} for request ##{request_id}")
        
        if packet_number < packet_count
          @buffer.clear
          bytes_read = @channel.read @buffer
          @buffer.rewind
          @buffer.limit bytes_read
        end
      end while packet_number < packet_count && @buffer.get_long == -2
      
      packet = SteamPacket.create_packet(split_packets.join(""))
        
    else
       
      packet = SteamPacket.create_packet(@buffer.get)
      
    end
    
    debug "Got reply of type \"#{packet.class.to_s}\"."
    
    return packet
  end
  
  def send(data_packet)
    debug "Sending dataü packet of type \"#{data_packet.class.to_s}\"."
    
    @buffer = ByteBuffer.wrap data_packet.to_s
    @channel.write @buffer
  end
  
end
