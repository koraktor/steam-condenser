# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'helper'
require 'steam/servers/server'

class TestServer < Test::Unit::TestCase

  context 'A generic server' do

    class GenericServer
      include Server
    end

    should 'split IP and port combinations' do
      Socket.expects(:getaddrinfo).
        with('127.0.0.1', '27015', Socket::AF_INET, Socket::SOCK_DGRAM).
        returns []
      Socket.expects(:getaddrinfo).
        with('someserver', '27015', Socket::AF_INET, Socket::SOCK_DGRAM).
        returns []

      GenericServer.new '127.0.0.1:27015'
      GenericServer.new 'someserver:27015'
    end

    should 'resolve multiple DNS names' do
      Socket.expects(:getaddrinfo).
        with('someserver', 27015, Socket::AF_INET, Socket::SOCK_DGRAM).
        returns [
          [nil, nil, 'someserver', '127.0.0.1'],
          [nil, nil, 'someserver2', '127.0.0.2']
        ]

      server = GenericServer.new 'someserver', 27015

      assert_equal %w{someserver someserver2}, server.host_names
      assert_equal %w{127.0.0.1 127.0.0.2}, server.ip_addresses
    end

    should 'rotate through multiple IP addresses' do
      Socket.stubs(:getaddrinfo).returns []

      server = GenericServer.new 'someserver'
      server.instance_variable_set :@ip_addresses, %w{127.0.0.1 127.0.0.2}
      server.instance_variable_set :@ip_address, '127.0.0.1'
      server.expects(:init_socket).twice

      assert_not server.rotate_ip
      assert_equal '127.0.0.2', server.instance_variable_get(:@ip_address)
      assert server.rotate_ip
      assert_equal '127.0.0.1', server.instance_variable_get(:@ip_address)
    end

    should 'allow a failsafe block to execute with IP fallback' do
      Socket.stubs(:getaddrinfo).returns []
      block = Proc.new { raise 'error' }

      server = GenericServer.new 'someserver'
      server.expects(:rotate_ip).twice.returns(false).then.returns(true)

      error = assert_raises RuntimeError do
        server.send :failsafe, &block
      end
      assert_equal 'error', error.message
    end

  end

end
