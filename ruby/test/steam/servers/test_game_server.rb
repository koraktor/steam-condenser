# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'helper'
require 'steam/servers/game_server'

class TestGameServer < Test::Unit::TestCase

  context 'A generic game server' do

    class GenericGameServer
      include GameServer
    end

    setup do
      Socket.stubs(:getaddrinfo).
        with('game', 27015, Socket::AF_INET, Socket::SOCK_DGRAM).
        returns [[nil, nil, 'game', '127.0.0.1']]
      @socket = mock

      @server = GenericGameServer.new 'game', 27015
      @server.instance_variable_set :@socket, @socket
    end

    should 'send packets using its client socket' do
      packet = mock
      @socket.expects(:send).with packet

      @server.send :send_request, packet
    end

    should 'get replies using its client socket' do
      packet = mock
      @socket.expects(:reply).returns packet

      assert_equal packet, @server.send(:reply)
    end

    should 'be able to calculate the latency of the server' do
      @server.expects(:send_request).with do |packet|
        packet.is_a? A2S_INFO_Packet
      end
      @server.expects(:reply).with { || sleep 0.05 }

      @server.update_ping
      assert_operator @server.instance_variable_get(:@ping), :>=, 50
    end

    should 'be able to get a challenge from the server' do
      @server.expects(:handle_response_for_request).with :challenge

      @server.update_challenge_number
    end

    should 'be able to get information about server' do
      @server.expects(:handle_response_for_request).with :info

      @server.update_server_info
    end

    should 'be able to get the server rules' do
      @server.expects(:handle_response_for_request).with :rules

      @server.update_rules
    end

    should 'be able to get the players on the server' do
      @server.expects(:handle_response_for_request).with :players

      @server.update_players
    end

    should 'provide a convenience wrapper for basic server methods' do
      @server.expects :update_ping
      @server.expects :update_server_info
      @server.expects :update_challenge_number

      @server.init
    end

    should 'know if its RCON connection is authenticated' do
      assert_equal @server.instance_variable_get(:@rcon_authenticated),
                   @server.rcon_authenticated?
    end

    should 'cache the ping of the server' do
      @server.expects :update_ping

      @server.ping

      @server.instance_variable_set :@ping, 0

      @server.ping
    end

    should 'cache the players on the server' do
      @server.expects :update_players

      @server.players

      @server.instance_variable_set :@player_hash, 0

      @server.players
    end

    should 'cache the server rules' do
      @server.expects :update_rules

      @server.rules

      @server.instance_variable_set :@rules_hash, 0

      @server.rules
    end

    should 'cache the server information' do
      @server.expects :update_server_info

      @server.server_info

      @server.instance_variable_set :@info_hash, 0

      @server.server_info
    end

    context 'be able to get additional information about the players on a Source server' do

      setup do
        status = fixture 'status_source'

        @someone = mock
        @somebody = mock
        player_hash = { 'someone' => @someone, 'somebody' => @somebody }
        @server.instance_variable_set :@player_hash, player_hash

        @server.expects(:handle_response_for_request).with :players
        @server.expects(:rcon_exec).with('status').returns status

        someone_data = { :name => 'someone', :userid => '1', :uniqueid => 'STEAM_0:0:123456', :score => '10', :time => '3:52', :ping => '12', :loss => '0', :state => 'active' }
        somebody_data = { :name => 'somebody', :userid => '2', :uniqueid => 'STEAM_0:0:123457', :score => '3', :time => '2:42', :ping => '34', :loss => '0', :state => 'active' }

        attributes = mock
        GameServer.expects(:player_status_attributes).
          with('userid name           uniqueid            score connected ping loss state').
          returns attributes
        GameServer.expects(:split_player_status).
          with(attributes, '1 "someone"      STEAM_0:0:123456    10    3:52      12   0    active').
          returns somebody_data
        GameServer.expects(:split_player_status).
          with(attributes, '2 "somebody"     STEAM_0:0:123457    3     2:42      34   0    active').
          returns someone_data

        @somebody.expects(:add_info).with somebody_data
        @someone.expects(:add_info).with someone_data
      end

      should 'with the RCON password' do
        @server.expects(:rcon_auth).with 'password'

        @server.update_players 'password'
      end

      should 'if the RCON connection is authenticated' do
        @server.instance_variable_set :@rcon_authenticated, true

        @server.update_players
      end

    end

    context 'be able to get additional information about the players on a GoldSrc server' do

      setup do
        status = fixture 'status_goldsrc'

        @someone = mock
        @somebody = mock
        player_hash = { 'someone' => @someone, 'somebody' => @somebody }
        @server.instance_variable_set :@player_hash, player_hash

        @server.expects(:handle_response_for_request).with :players
        @server.expects(:rcon_exec).with('status').returns status

        someone_data = { :name => 'someone', :userid => '1', :uniqueid => 'STEAM_0:0:123456', :score => '10', :time => '3:52', :ping => '12', :loss => '0', :adr => '0' }
        somebody_data = { :name => 'somebody', :userid => '2', :uniqueid => 'STEAM_0:0:123457', :score => '3', :time => '2:42', :ping => '34', :loss => '0', :adr => '0' }

        attributes = mock
        GameServer.expects(:player_status_attributes).
          with('name userid uniqueid frag time ping loss adr').
          returns attributes
        GameServer.expects(:split_player_status).
          with(attributes, '1   "someone" 1 STEAM_0:0:123456 10 3:52 12 0 0').
          returns somebody_data
        GameServer.expects(:split_player_status).
          with(attributes, '2   "somebody" 2 STEAM_0:0:123457 3 2:42 34 0 0').
          returns someone_data

        @somebody.expects(:add_info).with somebody_data
        @someone.expects(:add_info).with someone_data
      end

      should 'with the RCON password' do
        @server.expects(:rcon_auth).with 'password'

        @server.update_players 'password'
      end

      should 'if the RCON connection is authenticated' do
        @server.instance_variable_set :@rcon_authenticated, true

        @server.update_players
      end

    end

    should 'handle challenge requests' do
      @server.expects(:send_request).with do |packet|
        packet.is_a? A2S_SERVERQUERY_GETCHALLENGE_Packet
      end

      packet = mock
      packet.expects(:kind_of?).with(S2A_INFO_BasePacket).returns false
      packet.expects(:kind_of?).with(S2A_PLAYER_Packet).returns false
      packet.expects(:kind_of?).with(S2A_RULES_Packet).returns false
      packet.expects(:kind_of?).with(S2C_CHALLENGE_Packet).twice.returns true

      packet.expects(:challenge_number).returns 1234
      @server.expects(:reply).returns packet

      @server.handle_response_for_request :challenge

      assert_equal 1234, @server.instance_variable_get(:@challenge_number)
    end

    should 'handle info requests' do
      @server.expects(:send_request).with do |packet|
        packet.is_a? A2S_INFO_Packet
      end

      packet = mock
      packet.expects(:kind_of?).with(S2A_INFO_BasePacket).twice.returns true
      packet.expects(:info_hash).returns({ :test => 'test' })
      @server.expects(:reply).returns packet

      @server.handle_response_for_request :info

      assert_equal({ :test => 'test' }, @server.instance_variable_get(:@info_hash))
    end

    should 'server rule requests' do
      @server.expects(:send_request).with do |packet|
        packet.is_a? A2S_RULES_Packet
      end

      packet = mock
      packet.expects(:kind_of?).with(S2A_INFO_BasePacket).returns false
      packet.expects(:kind_of?).with(S2A_PLAYER_Packet).returns false
      packet.expects(:kind_of?).with(S2A_RULES_Packet).twice.returns true
      packet.expects(:rules_hash).returns({ :test => 'test' })
      @server.expects(:reply).returns packet

      @server.handle_response_for_request :rules

      assert_equal({ :test => 'test' }, @server.instance_variable_get(:@rules_hash))
    end

    should 'player requests' do
      @server.expects(:send_request).with do |packet|
        packet.is_a? A2S_PLAYER_Packet
      end

      packet = mock
      packet.expects(:kind_of?).with(S2A_INFO_BasePacket).returns false
      packet.expects(:kind_of?).with(S2A_PLAYER_Packet).twice.returns true
      packet.expects(:player_hash).returns({ :test => 'test' })
      @server.expects(:reply).returns packet

      @server.handle_response_for_request :players

      assert_equal({ :test => 'test' }, @server.instance_variable_get(:@player_hash))
    end

    should 'handle unexpected answers and retry' do
      @server.expects(:send_request).twice.with do |packet|
        packet.is_a? A2S_PLAYER_Packet
      end

      packet1 = mock
      packet1.expects(:kind_of?).with(S2A_INFO_BasePacket).returns true
      packet1.expects(:kind_of?).with(S2A_PLAYER_Packet).returns false
      packet1.expects(:info_hash).returns({ :test => 'test1' })
      packet2 = mock
      packet2.expects(:kind_of?).with(S2A_INFO_BasePacket).returns false
      packet2.expects(:kind_of?).with(S2A_PLAYER_Packet).twice.returns true
      packet2.expects(:player_hash).returns({ :test => 'test2' })
      @server.expects(:reply).twice.returns(packet1).returns packet2

      @server.handle_response_for_request :players

      assert_equal({ :test => 'test1' }, @server.instance_variable_get(:@info_hash))
      assert_equal({ :test => 'test2' }, @server.instance_variable_get(:@player_hash))
    end

  end

end
