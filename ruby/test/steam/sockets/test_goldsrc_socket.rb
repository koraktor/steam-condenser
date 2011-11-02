# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'helper'
require 'steam/sockets/goldsrc_socket'

class TestGoldSrcSocket < Test::Unit::TestCase

  context 'A new GoldSrc socket' do

    should 'know if its endpoint is a HLTV server' do
      socket1 = GoldSrcSocket.new '127.0.0.1', 27015
      socket2 = GoldSrcSocket.new '127.0.0.1', 27015, true

      assert_not socket1.instance_variable_get(:@is_hltv)
      assert socket2.instance_variable_get(:@is_hltv)
    end

  end

  context 'A GoldSrc socket' do

    setup do
      @socket = GoldSrcSocket.new '127.0.0.1'
    end

    should 'send wrapped up RCON requests' do
      packet = mock
      RCONGoldSrcRequest.expects(:new).with('test').returns packet
      @socket.expects(:send).with packet

      @socket.rcon_send 'test'
    end

    should 'be able to get a RCON challenge' do
      @socket.expects(:rcon_send).with 'challenge rcon'

      reply = mock :response => 'hallenge rcon 12345678'
      @socket.expects(:reply).returns reply

      @socket.rcon_challenge

      assert_equal '12345678', @socket.instance_variable_get(:@rcon_challenge)
    end

    should 'raise an error if the client is banned on challenge' do
      @socket.expects(:rcon_send).with 'challenge rcon'

      reply = mock :response => 'You have been banned from this server.'
      @socket.expects(:reply).returns reply

      assert_raises RCONBanError do
        @socket.rcon_challenge
      end
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
      buffer.expects(:byte).twice.returns(0x02).returns 0x12
      buffer.expects(:get).twice.returns(data1).returns(data2)

      packet = mock
      SteamPacketFactory.expects(:reassemble_packet).with([data1, data2]).
        returns packet

      assert_equal packet, @socket.reply
    end

    should 'be able to send RCON commands to game servers' do
      @socket.expects(:rcon_challenge).with do
        @socket.instance_variable_set :@rcon_challenge, '1234'
      end
      @socket.expects(:rcon_send).with("rcon 1234 password command")
      @socket.expects(:rcon_send).with("rcon 1234 password")
      packet1 = mock :response => 'test '
      packet2 = mock :response => 'test'
      packet3 = mock :response => ''
      @socket.expects(:reply).times(3).returns(packet1).returns(packet2).
        returns packet3

      assert_equal 'test test', @socket.rcon_exec('password', 'command')
    end

    should 'be able to send RCON commands to HLTV servers' do
      @socket.instance_variable_set :@is_hltv, true

      @socket.expects(:rcon_challenge).with do
        @socket.instance_variable_set :@rcon_challenge, '1234'
      end
      @socket.expects(:rcon_send).with("rcon 1234 password command")
      @socket.expects(:rcon_send).with("rcon 1234 password")
      packet1 = mock :response => 'test '
      packet2 = mock :response => 'test'
      packet3 = mock :response => ''
      @socket.expects(:reply).times(4).raises(SteamCondenser::TimeoutError).
        returns(packet1).returns(packet2).returns packet3

      assert_equal 'test test', @socket.rcon_exec('password', 'command')
    end

  end

end
