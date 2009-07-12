# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "abstract_class"
require "byte_buffer"
require "datagram_channel"
require "ipaddr"
require "exceptions/timeout_exception"

# The SteamSocket class is a sub class of CondenserSocket respecting the
# specifications of the Source query protocol.
class SteamSocket
  
  include AbstractClass
  
  def initialize(*args)
    @channel = DatagramChannel.open
    @channel.connect *args
    @channel.configure_blocking false
    
    @remote_socket = Socket.getaddrinfo args[0].to_s, args[1]
  end

  # Abstract get_reply method
  def get_reply
    raise NotImplementedError
  end
  
  def receive_packet(buffer_length = 0)
    if select([@channel.socket], nil, nil, 1) == nil
        raise TimeoutException.new
    end

    if buffer_length == 0
      @buffer.clear
    else
      @buffer = ByteBuffer.allocate buffer_length
    end
    
    bytes_read = @channel.read(@buffer)
    @buffer.rewind
    @buffer.limit = bytes_read
    
    bytes_read
  end
  
  def send(data_packet)
    warn "Sending data packet of type \"#{data_packet.class.to_s}\"."
    
    @buffer = ByteBuffer.wrap(data_packet.to_s)
    @channel.write(@buffer)
  end
  
  def finalize
    @channel.close
  end
  
end
