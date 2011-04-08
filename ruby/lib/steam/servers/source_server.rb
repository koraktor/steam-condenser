# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/rcon_no_auth_exception'
require 'steam/packets/rcon/rcon_auth_request'
require 'steam/packets/rcon/rcon_auth_response'
require 'steam/packets/rcon/rcon_exec_request'
require 'steam/packets/rcon/rcon_terminator'
require 'steam/servers/game_server'
require 'steam/sockets/rcon_socket'
require 'steam/sockets/source_socket'

class SourceServer

  include GameServer

  def initialize(address, port = 27015)
    super
  end

  # Initializes the sockets to communicate with the Source server
  def init_socket
    @rcon_socket = RCONSocket.new @ip_address, @port
    @socket      = SourceSocket.new @ip_address, @port
  end

  def rcon_auth(password)
    @rcon_request_id = rand 2**16

    @rcon_socket.send RCONAuthRequest.new(@rcon_request_id, password)
    @rcon_socket.reply
    reply = @rcon_socket.reply

    raise RCONNoAuthException.new if reply.request_id == -1

    reply.request_id == @rcon_request_id
  end

  def rcon_exec(command)
    @rcon_socket.send RCONExecRequest.new(@rcon_request_id, command)
    @rcon_socket.send RCONTerminator.new(@rcon_request_id)
    response_packets = []

    begin
      response_packet = @rcon_socket.reply
      redo if response_packet.nil?
      raise RCONNoAuthException.new if response_packet.is_a? RCONAuthResponse
      response_packets << response_packet
    end while response_packet.response.size > 0

    response = ''
    response_packets.each do |packet|
      response << packet.response
    end

    response.strip
  end

end
