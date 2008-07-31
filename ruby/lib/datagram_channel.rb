autoload "ByteBuffer", "byte_buffer"

class DatagramChannel
  def self.open
    return DatagramChannel.new
  end
  
  def initialize
    @socket = UDPSocket.new
    self.configure_blocking true
  end
  
  def connect(ipAddress, portNumber)
    @socket.connect ipAddress, portNumber
  end
  
  def configure_blocking(do_block)
  end
  
  def read(destination_buffer)
    if destination_buffer.class != ByteBuffer
      raise ArgumentError
    end
    
    length = destination_buffer.remaining
    
    destination_buffer.put @socket.recvfrom(length)
    
    return length
  end
  
  def write(source_buffer)
    return @socket.send(source_buffer.get, 0)
  end
end
