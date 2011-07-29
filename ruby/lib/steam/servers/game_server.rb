# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/steam_condenser_exception'
require 'steam/steam_player'
require 'steam/packets/a2s_info_packet'
require 'steam/packets/a2s_player_packet'
require 'steam/packets/a2s_rules_packet'
require 'steam/packets/a2s_serverquery_getchallenge_packet'
require 'steam/packets/s2a_info_base_packet'
require 'steam/packets/s2a_player_packet'
require 'steam/packets/s2a_rules_packet'
require 'steam/packets/s2c_challenge_packet'
require 'steam/servers/server'

# This module is included by classes representing different game server
# implementations and provides the basic functionality to communicate with
# them using the common query protocol
#
# @author Sebastian Staudt
module GameServer

  include Server

  # Parses the player attribute names supplied by `rcon status`
  #
  # @param [String] status_header The header line provided by `rcon status`
  # @return [Array<Symbol>] Split player attribute names
  # @see .split_player_status
  def self.player_status_attributes(status_header)
    status_header.split.map do |attribute|
      case attribute
        when 'connected'
          :time
        when 'frag'
          :score
        else
          attribute.to_sym
      end
    end
  end

  # Splits the player status obtained with `rcon status`
  #
  # @param [Array<Symbol>] attributes The attribute names
  # @param [String] player_status The status line of a single player
  # @return [Hash<Symbol, String>] The attributes with the corresponding values
  #         for this player
  # @see .player_status_attributes
  def self.split_player_status(attributes, player_status)
    player_status.sub! /^\d+ +/, '' if attributes.first != :userid

    first_quote = player_status.index '"'
    last_quote  = player_status.rindex '"'
    data = [
      player_status[0, first_quote],
      player_status[first_quote + 1..last_quote - 1],
      player_status[last_quote + 1..-1]
    ]
    data = [ data[0].split, data[1], data[2].split ]
    data.flatten!

    if attributes.size > data.size && attributes.include?(:state)
      data.insert 3, nil, nil, nil
    elsif attributes.size < data.size
      data.delete_at 1
    end

    player_data = {}
    data.each_index do |i|
      player_data[attributes[i]] = data[i]
    end

    player_data
  end

  # Creates a new instance of a game server object
  #
  # @param [String] address Either an IP address, a DNS name or one of them
  #        combined with the port number. If a port number is given, e.g.
  #        'server.example.com:27016' it will override the second argument.
  # @param [Fixnum] port The port the server is listening on
  # @raise [SteamCondenserException] if an host name cannot be resolved
  def initialize(address, port = 27015)
    super

    @rcon_authenticated = false
  end

  # Returns the last measured response time of this server
  #
  # If the latency hasn't been measured yet, it is done when calling this
  # method for the first time.
  #
  # If this information is vital to you, be sure to call {#update_ping}
  # regularly to stay up-to-date.
  #
  # @return [Fixnum] The latency of this server in milliseconds
  # @see #update_ping
  def ping
    update_ping if @ping.nil?
    @ping
  end

  # Returns a list of players currently playing on this server
  #
  # If the players haven't been fetched yet, it is done when calling this
  # method for the first time.
  #
  # As the players and their scores change quite often be sure to update this
  # list regularly by calling {#update_players} if you rely on this
  # information.
  #
  # @param [String] rcon_password The RCON password of this server may be
  #        provided to gather more detailed information on the players, like
  #        STEAM_IDs.
  # @return [Hash] The players on this server
  # @see update_players
  def players(rcon_password = nil)
    update_players(rcon_password) if @player_hash.nil?
    @player_hash
  end

  # Authenticates the connection for RCON communication with the server
  #
  # @abstract Must be be implemented by including classes to handle the
  #           authentication
  # @param [String] password The RCON password of the server
  # @return [Boolean] whether the authentication was successful
  # @see #rcon_exec
  def rcon_auth(password)
    raise NotImplementedError
  end

  # Returns whether the RCON connection to this server is already authenticated
  #
  # @return [Boolean] `true` if the RCON connection is authenticated
  # @see #rcon_auth
  def rcon_authenticated?
    @rcon_authenticated
  end

  # Remotely executes a command on the server via RCON
  #
  # @abstract Must be be implemented by including classes to handle the command
  #           execution
  # @param [String] command The command to execute on the server
  # @return [String] The output of the executed command
  # @see #rcon_auth
  def rcon_exec(command)
    raise NotImplementedError
  end

  # Returns the settings applied on the server. These settings are also called
  # rules.
  #
  # If the rules haven't been fetched yet, it is done when calling this method
  # for the first time.
  #
  # As the rules usually don't change often, there's almost no need to update
  # this hash. But if you need to, you can achieve this by calling
  # {#update_rules}.
  #
  # @return [Hash<String, String>] The currently active server rules
  # @see #update_rules
  def rules
    update_rules if @rules_hash.nil?
    @rules_hash
  end

  # Returns a hash with basic information on the server.
  #
  # If the server information haven't been fetched yet, it is done when
  # calling this method for the first time.
  #
  # The server information usually only changes on map change and when players
  # join or leave. As the latter changes can be monitored by calling
  # {#update_players}, there's no need to call {#update_server_info} very
  # often.
  #
  # @return [Hash] Server attributes with their values
  # @see #update_server_info
  def server_info
    update_server_info if @info_hash.nil?
    @info_hash
  end

  # Sends the specified request to the server and handles the returned response
  #
  # Depending on the given request type this will fill the various data
  # attributes of the server object.
  #
  # @param [Symbol] request_type The type of request to send to the server
  # @param [Boolean] repeat_on_failure Whether the request should be repeated,
  #        if the replied packet isn't expected. This is useful to handle
  #        missing challenge numbers, which will be automatically filled in,
  #        although not requested explicitly.
  # @raise [SteamCondenserException] if either the request type or the response
  #        packet is not known
  def handle_response_for_request(request_type, repeat_on_failure = true)
    begin
      case request_type
        when :challenge then
          request_packet = A2S_SERVERQUERY_GETCHALLENGE_Packet.new
          expected_response = S2C_CHALLENGE_Packet
        when :info then
          request_packet = A2S_INFO_Packet.new
          expected_response = S2A_INFO_BasePacket
        when :players then
          request_packet = A2S_PLAYER_Packet.new(@challenge_number)
          expected_response = S2A_PLAYER_Packet
        when :rules then
          request_packet = A2S_RULES_Packet.new(@challenge_number)
          expected_response = S2A_RULES_Packet
        else
          raise SteamCondenserException, 'Called with wrong request type.'
      end

      send_request request_packet
      response_packet = reply

      if response_packet.kind_of? S2A_INFO_BasePacket
        @info_hash = response_packet.info_hash
      elsif response_packet.kind_of? S2A_PLAYER_Packet
        @player_hash = response_packet.player_hash
      elsif response_packet.kind_of? S2A_RULES_Packet
        @rules_hash = response_packet.rules_hash
      elsif response_packet.kind_of? S2C_CHALLENGE_Packet
        @challenge_number = response_packet.challenge_number
      else
        raise SteamCondenserException, "Response of type #{response_packet.class} cannot be handled by this method."
      end

      unless response_packet.kind_of? expected_response
        puts "Expected #{expected_response}, got #{response_packet.class}." if $DEBUG
        handle_response_for_request(request_type, false) if repeat_on_failure
      end
    rescue TimeoutException
      puts "Expected #{expected_response}, but timed out." if $DEBUG
    end
  end

  # Initializes this server object with basic information
  #
  # @see #update_challenge_number
  # @see #update_ping
  # @see #update_server_info
  def init
    update_ping
    update_server_info
    update_challenge_number
  end

  # Sends a A2S_PLAYERS request to the server and updates the players' data for
  # this server
  #
  # As the players and their scores change quite often be sure to update this
  # list regularly by calling this method if you rely on this
  # information.
  #
  # @param [String] rcon_password The RCON password of this server may be
  #        provided to gather more detailed information on the players, like
  #        STEAM_IDs.
  # @see #handle_response_for_request
  # @see #players
  def update_players(rcon_password = nil)
    handle_response_for_request :players

    unless @rcon_authenticated
      return if rcon_password.nil?
      rcon_auth rcon_password
    end

    players = rcon_exec('status').lines.select do |line|
      line.start_with?('#') && line != "#end\n"
    end.map do |line|
      line[1..-1].strip
    end
    attributes = GameServer.player_status_attributes players.shift

    players.each do |player|
      player_data = GameServer.split_player_status(attributes, player)
      if @player_hash.key? player_data[:name]
        @player_hash[player_data[:name]].add_info player_data
      end
    end
  end

  # Sends a A2S_RULES request to the server and updates the rules of this
  # server
  #
  # As the rules usually don't change often, there's almost no need to update
  # this hash. But if you need to, you can achieve this by calling this method.
  #
  # @see #handle_response_for_request
  # @see #rules
  def update_rules
    handle_response_for_request :rules
  end

  # Sends a A2S_INFO request to the server and updates this server's basic
  # information
  #
  # The server information usually only changes on map change and when players
  # join or leave. As the latter changes can be monitored by calling
  # {#update_players}, there's no need to call this method very often.
  #
  # @see #handle_response_for_request
  # @see #server_info
  def update_server_info
    handle_response_for_request :info
  end

  # Sends a A2S_SERVERQUERY_GETCHALLENGE request to the server and updates the
  # challenge number used to communicate with this server
  #
  # There's usually no need to call this method explicitly, because
  # {#handle_response_for_request} will automatically get the challenge number
  # when the server assigns a new one.
  #
  # @see #handle_response_for_request
  # @see #init
  def update_challenge_number
    handle_response_for_request :challenge
  end

  # Sends a A2S_INFO request to the server and measures the time needed for the
  # reply
  #
  # If this information is vital to you, be sure to call this method regularly
  # to stay up-to-date.
  #
  # @return [Fixnum] The latency of this server in milliseconds
  # @see #ping
  def update_ping
    send_request A2S_INFO_Packet.new
    start_time = Time.now
    reply
    end_time = Time.now
    @ping = (end_time - start_time) * 1000
  end

  # Returns a human-readable text representation of the server
  #
  # @return [String] Available information about the server in a human-readable
  #         format
  def to_s
    return_string = ''

    return_string << "Ping: #{@ping}\n"
    return_string << "Challenge number: #{@challenge_number}\n"

    unless @info_hash.nil?
      return_string << "Info:\n"
      @info_hash.each do |key, value|
        return_string << "  #{key}: #{value.inspect}\n"
      end
    end

    unless @player_hash.nil?
      return_string << "Players:\n"
      @player_hash.each_value do |player|
        return_string << "  #{player}\n"
      end
    end

    unless @rules_hash.nil?
      return_string << "Rules:\n"
      @rules_hash.each do |key, value|
        return_string << "  #{key}: #{value}\n"
      end
    end

    return_string
  end

  protected

  # Receives a response from the server
  #
  # @return [SteamPacket] The response packet replied by the server
  def reply
    @socket.reply
  end

  # Sends a request packet to the server
  #
  # @param [SteamPacket] packet The request packet to send to the server
  def send_request(packet)
    @socket.send packet
  end

end
