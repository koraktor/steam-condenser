# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/servers/game_server'
require 'steam/servers/master_server'
require 'steam/sockets/goldsrc_socket'

# This class represents a GoldSrc game server and can be used to query
# information about and remotely execute commands via RCON on the server
#
# A GoldSrc game server is an instance of the Half-Life Dedicated Server (HLDS)
# running games using Valve's GoldSrc engine, like Half-Life Deathmatch,
# Counter-Strike 1.6 or Team Fortress Classic.
#
# @author Sebastian Staudt
# @see SourceServer
class GoldSrcServer

  include GameServer

  # Returns a master server instance for the default master server for GoldSrc
  # games
  #
  # @return [MasterServer] The GoldSrc master server
  def self.master
    MasterServer.new *MasterServer::GOLDSRC_MASTER_SERVER
  end

  # Creates a new instance of a GoldSrc server object
  #
  # @param [String] address Either an IP address, a DNS name or one of them
  #        combined with the port number. If a port number is given, e.g.
  #        'server.example.com:27016' it will override the second argument.
  # @param [Fixnum] port The port the server is listening on
  # @raise [SteamCondenserError] if an host name cannot be resolved
  # @param [Boolean] is_hltv HLTV servers need special treatment, so this is
  #        used to determine if the server is a HLTV server
  def initialize(address, port = 27015, is_hltv = false)
    super address, port

    @is_hltv = is_hltv
  end

  # Initializes the socket to communicate with the GoldSrc server
  #
  # @see GoldSrcSocket
  def init_socket
    @socket = GoldSrcSocket.new @ip_address, @port, @is_hltv
  end

  # Saves the password for authenticating the RCON communication with the
  # server
  #
  # @param [String] password The RCON password of the server
  # @return [true] GoldSrc's RCON does not preauthenticate connections so this
  #         method always returns `true`
  # @see #rcon_exec
  def rcon_auth(password)
    @rcon_password = password
    true
  end

  # Remotely executes a command on the server via RCON
  #
  # @param [String] command The command to execute on the server via RCON
  # @return [String] The output of the executed command
  # @see #rcon_auth
  def rcon_exec(command)
    @socket.rcon_exec(@rcon_password, command).strip
  end

end
