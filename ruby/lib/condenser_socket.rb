# The CondensorSocket class is a wrapper around the UDPSocket class of the Ruby Standard library.
# It includes a read buffer and some comfortable functions to read and write to this buffer. 
class CondenserSocket

  # Creates a new CondensorSocket by IP and port number
  def initialize(ip_address, port_number)
    @ip_address = ip_address
    @port_number = port_number
    @read_buffer = ""
    @socket = UDPSocket.new
  end
  
  # Closes the socket on destruction by the garbage collector
  def finalize
    @socket.close
  end
  
  # Returns all contents from the read buffer
  def flush_buffer
    return self.read(@read_buffer.size).to_s
  end
  
  # Returns an 8 bit character from the read buffer
  def get_byte
    return self.read(1).unpack("c")[0]
  end
  
  # Returns a 32 bit floating point number from the read buffer
  def get_float
    return self.read(4).to_f
  end
  
  # Returns a 32 bit signed integer from the read buffer 
  def get_long
    return self.read(4).unpack("l")[0]
  end
  
  # Returns a 16 bit signed integer from the read buffer
  def get_short
    return self.read(2).to_i
  end
  
  # Reads the specified number of characters from the read buffer and returns them
  def read(length = 1)
    if @read_buffer == nil
      raise Exception.new("No data to read.")
    end
    
    reply_data = @read_buffer[0, length]
    @read_buffer[0, length] = ""
    
    return reply_data
  end
  
  # Reads the specified number of characters from the socket and saves them in the read buffer
  def read_to_buffer(length = 128)
    begin
      if select([@socket], nil, nil, 1)
      	data_read = @socket.recv length
      end
 
      debug "Received data: #{data_read}"
    
      @read_buffer = data_read
    rescue Exception
      raise Exception.new("Fehler beim Lesen der Daten. #{$!.message}")
    end
  end
  
  # Sends a string representation of an object to the socket endpoint 
  def send(data)
    puts "Sending data packet of type \"#{data.class.to_s}\"."
    debug "Send data: #{data.to_s}"
    @socket.send data.to_s, 0, @ip_address.to_s, @port_number
  end
  
end
