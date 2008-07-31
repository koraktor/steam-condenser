class ByteBuffer
  def self.allocate(length)
    return ByteBuffer.new 0.chr * length
  end
  
  def self.wrap(byte_buffer)
    byte_buffer = ByteBuffer.new byte_buffer
  end
  
  def initialize(byte_array)
    @byte_array = byte_array
    @capacity = byte_array.length
    @limit = @capacity
    @position = 0
    @mark = -1
  end
  
  def clear
    @limit = @capacity
    @position = 0
    @mark = -1
    
    return self
  end
  
  def flip
    @limit = @position
    @position = 0
    @mark = -1
    
    return self
  end
  
  def get(length = nil)
    if length == nil
      length = @limit - @position
    end
    
    data = @byte_array[@position, length]
    @position += length
    
    return data
  end
  
  def get_byte
    return self.get(1)[0]
  end
  
  def get_long
    return self.get(4).unpack("l")[0]
  end

  def get_short
    return self.get(2).unpack("v")[0]
  end
  
  def get_string
    return self.get(@byte_array.index("\0", @position) - @position + 1)
  end
  
  def limit(new_limit = nil)
    if new_limit == nil
      return @limit
    else
      @limit = new_limit
      
      return self
    end
  end
  
  def remaining
    return @limit - @position
  end
  
  def rewind
    @position = 0
    @mark = -1
    
    return self
  end
  
  def put(source_byte_array)
    if source_byte_array.length > self.remaining
      new_position = self.remaining
    else
      new_position = source_byte_array.length
    end
    
    @byte_array[@position, new_position] = source_byte_array
    @position = new_position
    
    return self
  end
  
end
