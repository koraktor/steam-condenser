class CondenserSocket
  @ip_address = nil
  @port_number = nil
  @read_buffer = ""
  @socket = nil
  
  def initialize(ip_address, port_number)
    @ip_address = ip_address
    @port_number = port_number
    @socket = UDPSocket.new 
  end
  
  def finalize
    @socket.close
  end
  
  def flush_buffer
    return self.read(@read_buffer.size).to_s
  end
  
  def get_byte
    return self.read(1).to_i
  end
  
  def get_float
    return self.read(4).to_f
  end
  
  def get_long
    return self.read(4).to_i
  end
  
  def get_short
    return self.read(2).to_i
  end
  
  def read(length = 1)
    if @read_buffer.empty?
      raise Exception.new("No data to read.")
    end
    
    reply_data = @read_buffer[0, length]
    @read_buffer[0, length] = ""
    
    return reply_data
  end
  
  def read_to_buffer(length = 128)
    if select([@socket], nil, nil, 1)
      data_read = @socket.recvfrom length
    end
    
    @read_buffer = data_read[0]
  end
  
  def send(data)
    puts "Sending data packet of type \"#{data.class.to_s}\"."
    @socket.send data.to_s, 0, @ip_address.to_s, @port_number
  end
  
end