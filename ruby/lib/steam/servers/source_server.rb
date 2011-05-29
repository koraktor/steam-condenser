# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
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

# This class represents a Source game server and can be used to query
# information about and remotely execute commands via RCON on the server
#
# A Source game server is an instance of the Source Dedicated Server (SrcDS)
# running games using Valve's Source engine, like Counter-Strike: Source,
# Team Fortress 2 or Left4Dead.
#
# @author Sebastian Staudt
# @see GoldSrcServer
class SourceServer

  include GameServer

  # Creates a new instance of a server object representing a Source server,
  # i.e. SrcDS instance
  #
  # @param [String] address Either an IP address, a DNS name or one of them
  #        combined with the port number. If a port number is given, e.g.
  #        'server.example.com:27016' it will override the second argument.
  # @param [Fixnum] port The port the server is listening on
  # @raise [SteamCondenserException] if an host name cannot be resolved
  def initialize(address, port = 27015)
    super
  end

  # Initializes the sockets to communicate with the Source server
  #
  # @see RCONSocket
  # @see SourceSocket
  def init_socket
    @rcon_socket = RCONSocket.new @ip_address, @port
    @socket      = SourceSocket.new @ip_address, @port
  end

  # Authenticates the connection for RCON communication with the server
  #
  # @param [String] password The RCON password of the server
  # @return [Boolean] whether authentication was successful
  # @see #rcon_exec
  def rcon_auth(password)
    @rcon_request_id = rand 2**16

    @rcon_socket.send RCONAuthRequest.new(@rcon_request_id, password)
    @rcon_socket.reply
    reply = @rcon_socket.reply

    raise RCONNoAuthException.new if reply.request_id == -1

    reply.request_id == @rcon_request_id
  end

  # Remotely executes a command on the server via RCON
  #
  # @param [String] command The command to execute on the server via RCON
  # @return [String] The output of the executed command
  # @see #rcon_auth
  def rcon_exec(command)
    @rcon_socket.send RCONExecRequest.new(@rcon_request_id, command)
    @rcon_socket.send RCONTerminator.new(@rcon_request_id)

    response = ''
    begin
      response_packet = @rcon_socket.reply
      raise RCONNoAuthException.new if response_packet.is_a? RCONAuthResponse
      response << response_packet.response
    end while response.length == 0 || response_packet.response.size > 0

    response.strip
  end

end
