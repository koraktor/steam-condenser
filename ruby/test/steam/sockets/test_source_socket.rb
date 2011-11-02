# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'helper'
require 'steam/sockets/source_socket'

class TestSourceSocket < Test::Unit::TestCase

  context 'A Source socket' do

    setup do
      @socket = SourceSocket.new '127.0.0.1'
    end

    should 'be able to receive single packet replies' do
      buffer = mock
      @socket.instance_variable_set :@buffer, buffer

      @socket.expects(:receive_packet).with 1400
      data = mock
      buffer.expects(:long).returns 0xFFFFFFFF
      buffer.expects(:get).returns data
      packet = mock
      SteamPacketFactory.expects(:packet_from_data).with(data).returns packet

      assert_equal packet, @socket.reply
    end

    should 'be able to receive split packet replies' do
      buffer = mock
      @socket.instance_variable_set :@buffer, buffer

      @socket.expects(:receive_packet).with(1400)
      @socket.expects(:receive_packet).with().returns 1400

      data1, data2 = mock, mock
      buffer.expects(:long).times(4).returns(0xFFFFFFFE).returns(1234).
        returns(0xFFFFFFFE).returns 1234
      buffer.expects(:byte).times(4).returns(0x02).returns(0x00).returns(0x02).
        returns 0x01
      buffer.expects(:short).twice
      buffer.expects(:get).twice.returns(data1).returns(data2)

      packet = mock
      SteamPacketFactory.expects(:reassemble_packet).with([data1, data2]).
        returns packet

      assert_equal packet, @socket.reply
    end

    should 'be able to receive compressed replies' do
      buffer = mock
      @socket.instance_variable_set :@buffer, buffer

      @socket.expects(:receive_packet).with(1400)
      @socket.expects(:receive_packet).with().returns 1400

      data1, data2 = mock, mock
      buffer.expects(:long).times(8).returns(0xFFFFFFFE).returns(2147484882).
        returns(0).returns(1337).returns(0xFFFFFFFE).returns(2147484882).
        returns(0).returns 1337
      buffer.expects(:byte).times(4).returns(0x02).returns(0x00).returns(0x02).
        returns 0x01
      buffer.expects(:get).twice.returns(data1).returns(data2)

      packet = mock
      SteamPacketFactory.expects(:reassemble_packet).
        with([data1, data2], true, 1337).returns packet

      assert_equal packet, @socket.reply
    end

  end

end
