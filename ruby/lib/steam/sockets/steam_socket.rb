require "byte_buffer"
require "datagram_channel"

# The SteamSocket class is a sub class of CondenserSocket respecting the
# specifications of the Source query protocol.
class SteamSocket
  
  def initialize(ip_address, port_number)
    @channel = DatagramChannel.open
    @channel.connect ip_address.to_s, port_number
    @channel.configure_blocking false
  end

  # Abstract get_reply method
  def get_reply
    raise NotImplementedError
  end
  
  def receive_packet(buffer_length = 0)
    if buffer_length == 0
      @buffer.clear
      select([@channel.socket], nil, nil, 1)
    else
      @buffer = ByteBuffer.allocate buffer_length
      select([@channel.socket], nil, nil, 1)
    end
    
    @channel.read @buffer
    bytes_read = @buffer.position
    @buffer.rewind
    @buffer.limit = bytes_read
    
    return bytes_read
  end
  
  def send(data_packet)
    debug "Sending dataü packet of type \"#{data_packet.class.to_s}\"."
    
    @buffer = ByteBuffer.wrap data_packet.to_s
    @channel.write @buffer
  end
  
  def finalize
    @channel.close
  end
  
end
