# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "abstract_class"
require "exceptions/steam_condenser_exception"
require "steam/steam_player"
require "steam/packets/a2s_info_packet"
require "steam/packets/a2a_ping_packet"
require "steam/packets/a2s_player_packet"
require "steam/packets/a2s_rules_packet"
require "steam/packets/a2s_serverquery_getchallenge_packet"
require "steam/packets/s2a_info_base_packet"
require "steam/packets/s2a_player_packet"
require "steam/packets/s2a_rules_packet"
require "steam/packets/s2c_challenge_packet"

class GameServer
  
  include AbstractClass
  
  REQUEST_CHALLENGE = 0
  REQUEST_INFO = 1
  REQUEST_PLAYER = 2
  REQUEST_RULES = 3
  
  # Returns the last measured response time of this server
  #
  # If there's no data, update_ping is called to measure the current response
  # time of the server.
  #
  # Whenever you want to get a new value for the ping time call update_ping.
  def get_ping
    if @ping == nil
      self.update_ping
    end
    return @ping
  end
  
  # Returns an array of the player's currently playing on this server.
  #
  # If there's no data, update_player_info is called to get the current list of
  # players.
  #  
  # As the players and their scores change quite often be sure to update this
  # list regularly by calling update_player_info.
  def get_players(rcon_password = nil)
    if @player_hash == nil
      self.update_player_info(rcon_password)
    end
    
    return @player_hash
  end
  
  # Returns a hash of the settings applied on the server. These settings are
  # also called rules.
  # The hash has the format of +rule_name+ => +rule_value+
  # 
  # If there's no data, update_rules_info is called to get the current list of
  # rules.
  #
  # As the rules usually don't change often, there's almost no need to update
  # this hash. But if you need to, you can achieve this by calling
  # update_rules_info.
  def get_rules
    if @rules_hash == nil
      self.update_rules_info
    end
    
    return @rules_hash
  end
  
  # Returns a hash with basic information on the server.
  # 
  # If there's no data, update_server_info is called to get up-to-date
  # information.
  #
  # The server information usually only changes on map change and when players
  # join or leave. As the latter changes can be monitored by calling
  # update_player_info, there's no need to call update_server_info very often.
  def get_server_info
    if @info_hash == nil
      self.update_server_info
    end
    
    return @info_hash
  end
  
  def handle_response_for_request(request_type, repeat_on_failure = true)
    begin
      case request_type
        when GameServer::REQUEST_CHALLENGE then
          request_packet = A2S_SERVERQUERY_GETCHALLENGE_Packet.new
          expected_response = S2C_CHALLENGE_Packet
        when GameServer::REQUEST_INFO then
          request_packet = A2S_INFO_Packet.new
          expected_response = S2A_INFO_BasePacket
        when GameServer::REQUEST_PLAYER then
          request_packet = A2S_PLAYER_Packet.new(@challenge_number)
          expected_response = S2A_PLAYER_Packet
        when GameServer::REQUEST_RULES then
          request_packet = A2S_RULES_Packet.new(@challenge_number)
          expected_response = S2A_RULES_Packet
        else
          raise SteamCondenserException.new("Called with wrong request type.")
      end
      
      self.send_request request_packet
      response_packet = self.get_reply

      if response_packet.kind_of? S2A_INFO_BasePacket
        @info_hash = response_packet.get_info_hash
      elsif response_packet.kind_of? S2A_PLAYER_Packet
        @player_hash = response_packet.player_hash
      elsif response_packet.kind_of? S2A_RULES_Packet
        @rules_hash = response_packet.get_rules_hash
      elsif response_packet.kind_of? S2C_CHALLENGE_Packet
        @challenge_number = response_packet.get_challenge_number
      else
        raise SteamCondenserException.new("Response of type #{response_packet.class} cannot be handled by this method.")
      end
      
      unless response_packet.kind_of? expected_response
        warn "Expected #{expected_response}, got #{response_packet.class}."
        self.handle_response_for_request(request_type, false) if repeat_on_failure
      end
    rescue TimeoutException
      warn "Expected #{expected_response}, but timed out."
    end
  end
  
  def init
    self.update_ping
    self.update_server_info
    self.update_challenge_number
  end

  def update_player_info(rcon_password = nil)
    self.handle_response_for_request GameServer::REQUEST_PLAYER

    unless rcon_password.nil? or @player_hash.empty?
      rcon_auth(rcon_password)
      players = rcon_exec('status').split("\n")[7..-1]

      players.each do |player|
        player_data = split_player_status(player)
        @player_hash[player_data[1]].add_info(*player_data)
      end
    end
  end

  def update_rules_info
    self.handle_response_for_request GameServer::REQUEST_RULES
  end
  
  def update_server_info
    self.handle_response_for_request GameServer::REQUEST_INFO
  end
  
  def update_challenge_number
    self.handle_response_for_request GameServer::REQUEST_CHALLENGE
  end
 
  def update_ping
    self.send_request A2A_PING_Packet.new
    start_time = Time.now
    self.get_reply
    end_time = Time.now
    return @ping = (end_time - start_time) * 1000
  end
  
  # Checks whether the listening port number of the server is in a valid range 
  def initialize(port_number = 27015)
    unless port_number.to_i > 0 and port_number.to_i < 65536
      raise ArgumentError.new("The listening port of the server has to be a number greater than 0 and less than 65536.")
    end
  end
  
  def to_s
    return_string = ""
    
    return_string << "Ping: #{@ping}\n"
    return_string << "Challenge number: #{@challenge_number}\n"
    
    if @info_hash != nil
      return_string << "Info:\n"
      @info_hash.each do |key, value|
        return_string << "  #{key}: #{value.inspect}\n"
      end
    end
    
    if @player_hash != nil
      return_string << "Players:\n"
      @player_hash.each do |player|
        return_string << "  #{player}\n"
      end
    end
    
    if @rules_hash != nil
      return_string << "Rules:\n"
      @rules_hash.each do |key, value|
        return_string << "  #{key}: #{value}\n"
      end
    end
    
    return return_string
  end

  protected

  def get_reply
    @socket.get_reply
  end
  
  def send_request packet
    @socket.send packet
  end
end
