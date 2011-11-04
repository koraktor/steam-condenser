# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'helper'
require 'steam/sockets/master_server_socket'

class TestMasterServerSocket < Test::Unit::TestCase

  context 'A master server socket' do

    setup do
      @socket = MasterServerSocket.new '127.0.0.1'
    end

    should 'raise an error if the packet header is incorrect' do
      @socket.stubs :receive_packet
      @socket.instance_variable_get(:@buffer).expects(:long).returns 1

      error = assert_raises PacketFormatError do
        @socket.reply
      end
      assert_equal 'Master query response has wrong packet header.', error.message
    end

    should 'receive correct packets' do
      @socket.expects(:receive_packet).with 1500
      buffer = @socket.instance_variable_get :@buffer
      buffer.expects(:long).returns 0xFFFFFFFF
      buffer.expects(:get).returns 'test'

      packet = mock
      SteamPacketFactory.expects(:packet_from_data).with('test').returns packet

      assert_equal packet, @socket.reply
    end

  end

end
