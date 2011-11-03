# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'helper'
require 'steam/sockets/rcon_socket'

class TestRCONSocket < Test::Unit::TestCase

  context 'A new RCON socket' do

    should 'know its IP and port, but not open a connection' do
      socket = RCONSocket.new '127.0.0.1', 27015

      assert_equal IPAddr.new('127.0.0.1'), socket.instance_variable_get(:@ip)
      assert_equal 27015, socket.instance_variable_get(:@port)
      assert_nil socket.instance_variable_get(:@socket)
    end

  end

  context 'A disconnected RCON socket' do

    setup do
      @socket = RCONSocket.new '127.0.0.1', 27015
    end

    should 'establish the TCP connection when sending' do
      tcp_socket = mock
      tcp_socket.expects(:send).with 'test', 0
      @socket.expects(:connect).with do
        @socket.instance_variable_set :@socket, tcp_socket
      end

      @socket.send mock(:to_s => 'test')
    end

    should 're-establish the TCP connection when sending' do
      tcp_socket = mock
      tcp_socket.expects(:closed?).returns true
      tcp_socket.expects(:send).with 'test', 0
      @socket.expects :connect
      @socket.instance_variable_set :@socket, tcp_socket

      packet = mock
      packet.expects(:to_s).returns 'test'
      @socket.send packet
    end

    should 'be able to establish the TCP connection' do
      tcp_socket = mock
      TCPSocket.expects(:new).with('127.0.0.1', 27015).
        returns tcp_socket

      @socket.connect

      assert_equal tcp_socket, @socket.instance_variable_get(:@socket)
    end

    should 'raise a timeout if the connection cannot be established' do
      @socket.expects(:timeout).raises Timeout::Error

      assert_raises SteamCondenser::TimeoutError do
        @socket.connect
      end
    end

  end

  context 'A connected RCON socket' do

    setup do
      @socket = RCONSocket.new '127.0.0.1', 27015
      @tcp_socket = mock
      @socket.instance_variable_set :@socket, @tcp_socket
    end

    should 'be able to close the TCP connection' do
      @tcp_socket.expects :close

      @socket.close
    end

    should 'receive responses using the TCP connection' do
      buffer = mock
      @socket.instance_variable_set :@buffer, buffer

      buffer.expects :rewind
      buffer.expects(:long).returns 1234
      buffer.expects(:get).twice.returns('test ').returns 'test'
      @socket.expects(:receive_packet).with(4).returns 1
      @socket.expects(:receive_packet).with(1234).returns 1000
      @socket.expects(:receive_packet).with(234).returns 234
      RCONPacketFactory.expects(:packet_from_data).with 'test test'

      @socket.reply
    end

    should 'raise an error if the client has been banned' do
      @socket.expects(:receive_packet).with(4).returns 0

      assert_raises RCONBanError do
        @socket.reply
      end
    end

  end

end
