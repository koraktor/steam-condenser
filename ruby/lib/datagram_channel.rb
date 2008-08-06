autoload "ByteBuffer", "byte_buffer"

class DatagramChannel
  attr_reader :socket
  
  def self.open
    return DatagramChannel.new
  end
  
  def close
    @socket.close
  end
  
  def connect(*args)
    if args[0].is_a? IPAddr
      ip_address = args[0].to_s
    elsif args[0].is_a? String
      sockaddr = (Socket.gethostbyname args[0])[3]
      ip_address = "#{sockaddr[0]}.#{sockaddr[1]}.#{sockaddr[2]}.#{sockaddr[3]}"
    end
    
    if args[1].is_a? Numeric
      port_number = args[1]
    else
      port_number = args[1].to_i
    end
    
    @socket.connect ip_address, port_number
    
    return self
  end
  
  def configure_blocking(do_block)
  end
  
  def read(destination_buffer)
    if !destination_buffer.is_a? ByteBuffer
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
  
  protected
  
  def initialize
    @socket = UDPSocket.new
    self.configure_blocking true
  end
end
