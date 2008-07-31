autoload "ByteBuffer", "byte_buffer"

class DatagramChannel
  attr_reader :socket
  
  def self.open
    return DatagramChannel.new
  end
  
  def initialize
    @socket = UDPSocket.new
    self.configure_blocking true
  end
  
  def close
    @socket.close
  end
  
  def connect(ipAddress, portNumber)
    @socket.connect ipAddress, portNumber
    
    return self
  end
  
  def configure_blocking(do_block)
  end
  
  def read(destination_buffer)
    if destination_buffer.class != ByteBuffer
      raise ArgumentError
    end
    
    length = destination_buffer.remaining
    data = @socket.recv length
    destination_buffer.put data
    
    return data.length
  end
  
  def write(source_buffer)
    return @socket.send(source_buffer.get, 0)
  end
end
