class ByteBuffer
  def initialize(byte_array)
    @byte_array = byte_array
    @pointer = 0
  end
  
  def get(length)
    data = @byte_array[@pointer, length]
    @pointer += length
    return data
  end
  
  def get_byte
    return self.get(1)[0]
  end

  def get_short
    return self.get(2).unpack("v")[0]
  end
  
  def get_string
    return self.get(@byte_array.index("\0", @pointer) - @pointer + 1)
  end
end
