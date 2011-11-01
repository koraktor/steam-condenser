# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'helper'
require 'steam/servers/master_server'

class TestMasterServer < Test::Unit::TestCase

  context 'The user' do

    should 'be able to set the number of retries' do
      MasterServer.retries = 5

      assert_equal 5, MasterServer.send(:class_variable_get, :@@retries)
    end

  end

  context 'A master server' do

    setup do
      Socket.stubs(:getaddrinfo).
        with('master', 27015, Socket::AF_INET, Socket::SOCK_DGRAM).
        returns [[nil, nil, 'master', '127.0.0.1']]

      @server = MasterServer.new 'master', 27015
    end

    should 'create a client socket upon initialization' do
      socket = mock
      MasterServerSocket.expects(:new).with('127.0.0.1', 27015).returns socket

      @server.init_socket

      assert_same socket, @server.instance_variable_get(:@socket)
    end

    should 'be able to get a challenge' do
      reply = mock :challenge => 1234

      socket = @server.instance_variable_get :@socket
      socket.expects(:send).with { |packet| packet.is_a? C2M_CHECKMD5_Packet }
      socket.expects(:reply).returns reply

      assert_equal 1234, @server.challenge
    end

    should 'be able to get a list of servers' do
      reply1 = mock :servers => %w{127.0.0.1:27015 127.0.0.2:27015 127.0.0.3:27015}
      reply2 = mock :servers => %w{127.0.0.4:27015 0.0.0.0:0}

      socket = @server.instance_variable_get :@socket
      socket.expects(:send).with do |packet|
        packet.is_a?(A2M_GET_SERVERS_BATCH2_Packet) &&
        packet.instance_variable_get(:@filter) == 'filter' &&
        packet.instance_variable_get(:@region_code) == MasterServer::REGION_EUROPE &&
        packet.instance_variable_get(:@start_ip) == '0.0.0.0:0'
      end
      socket.expects(:send).with do |packet|
        packet.is_a?(A2M_GET_SERVERS_BATCH2_Packet) &&
        packet.instance_variable_get(:@filter) == 'filter' &&
        packet.instance_variable_get(:@region_code) == MasterServer::REGION_EUROPE &&
        packet.instance_variable_get(:@start_ip) == '127.0.0.3:27015'
      end
      socket.expects(:reply).times(2).returns(reply1).returns reply2

      servers = [['127.0.0.1', '27015'], ['127.0.0.2', '27015'], ['127.0.0.3', '27015'], ['127.0.0.4', '27015']]
      assert_equal servers, @server.servers(MasterServer::REGION_EUROPE, 'filter')
    end

    should 'be able to send a heartbeat' do
      reply1 = mock
      reply2 = mock

      socket = @server.instance_variable_get :@socket
      socket.expects(:send).with { |packet| packet.is_a? S2M_HEARTBEAT2_Packet }
      socket.expects(:reply).times(3).returns(reply1).returns(reply2).then.
        raises SteamCondenser::TimeoutError

      data = { :challenge => 1234 }
      assert_equal [reply1, reply2], @server.send_heartbeat(data)
    end

    should 'not timeout if returning servers is forced' do
      MasterServer.retries = 1

      reply = mock :servers => %w{127.0.0.1:27015 127.0.0.2:27015 127.0.0.3:27015}

      socket = @server.instance_variable_get :@socket
      socket.expects(:send).with do |packet|
        packet.is_a?(A2M_GET_SERVERS_BATCH2_Packet) &&
        packet.instance_variable_get(:@filter) == 'filter' &&
        packet.instance_variable_get(:@region_code) == MasterServer::REGION_EUROPE &&
        packet.instance_variable_get(:@start_ip) == '0.0.0.0:0'
      end
      socket.expects(:send).with do |packet|
        packet.is_a?(A2M_GET_SERVERS_BATCH2_Packet) &&
        packet.instance_variable_get(:@filter) == 'filter' &&
        packet.instance_variable_get(:@region_code) == MasterServer::REGION_EUROPE &&
        packet.instance_variable_get(:@start_ip) == '127.0.0.3:27015'
      end
      socket.expects(:reply).times(2).returns(reply).then.
        raises(SteamCondenser::TimeoutError)

      servers = [['127.0.0.1', '27015'], ['127.0.0.2', '27015'], ['127.0.0.3', '27015']]
      assert_equal servers, @server.servers(MasterServer::REGION_EUROPE, 'filter', true)
    end

    should 'timeout after a predefined number of retries' do
      retries = rand(4) + 1
      MasterServer.retries = retries

      socket = @server.instance_variable_get :@socket
      socket.expects(:send).times(retries).with do |packet|
        packet.is_a? A2M_GET_SERVERS_BATCH2_Packet
        packet.instance_variable_get(:@filter) == ''
        packet.instance_variable_get(:@region_code) == MasterServer::REGION_ALL
        packet.instance_variable_get(:@start_ip) == '0.0.0.0:0'
      end
      socket.expects(:reply).times(retries).raises SteamCondenser::TimeoutError

      assert_raises SteamCondenser::TimeoutError do
        @server.servers
      end
    end

  end

end
