# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

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
    if buffer_length == 0
      if select([@channel.socket], nil, nil, 0) == nil
        return 0
      end
      @buffer.clear
    else
      if select([@channel.socket], nil, nil, 1) == nil
        raise TimeoutException.new
      end
      @buffer = ByteBuffer.allocate buffer_length
    end
    
    @channel.read @buffer
    bytes_read = @buffer.position
    @buffer.rewind
    @buffer.limit = bytes_read
    
    return bytes_read
  end
  
  def send(data_packet)
    warn "Sending data packet of type \"#{data_packet.class.to_s}\"."
    
    @buffer = ByteBuffer.wrap data_packet.to_s
    @channel.write @buffer
  end
  
  def finalize
    @channel.close
  end
  
  protected
  
  def create_packet
    return SteamPacketFactory.get_packet_from_data(@buffer.get )
  end
  
end
