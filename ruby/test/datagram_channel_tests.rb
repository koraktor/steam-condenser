# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

$:.push File.join(File.dirname(__FILE__), '..', 'lib')

require 'test/unit'

require 'byte_buffer'
require 'datagram_channel'

class DatagramChannelTests < Test::Unit::TestCase

  def test_read_write
    port = rand(65536 - 1024) + 1024

    socket = UDPSocket.new
    socket.bind('localhost', port)

    channel = DatagramChannel.open
    channel.connect('localhost', port)

    string = 'test'
    
    buffer = ByteBuffer.wrap(string)
    channel.write(buffer)
    sent, socket_addr = socket.recvfrom(4)
    socket.send(string, 0, 'localhost', socket_addr[1])
    buffer = ByteBuffer.allocate(4)
    channel.read(buffer)

    channel.close
    socket.close

    received = buffer.array

    assert_equal(string, sent)
    assert_equal(string, received)
  end

end
