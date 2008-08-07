# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "abstract_class"
require "steam/steam_player"
require "steam/packets/a2a_info_request_packet"
require "steam/packets/a2a_ping_request_packet"
require "steam/packets/a2a_player_request_packet"
require "steam/packets/a2a_rules_request_packet"
require "steam/packets/a2a_serverquery_getchallenge_request_packet"

class GameServer
  
  include AbstractClass
  
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
  def get_players
    if @player_array == nil
      self.update_player_info
    end
    
    return @player_array
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
  
  def init
    update_ping
    update_server_info
    update_challenge_number
  end

  def update_player_info
    send_request A2A_PLAYER_RequestPacket.new(@challenge_number)
    @player_array = get_reply.get_player_array
  end

  def update_rules_info
    send_request A2A_RULES_RequestPacket.new(@challenge_number)
    @rules_hash = get_reply.get_rules_hash
  end
  
  def update_server_info
    send_request A2A_INFO_RequestPacket.new
    @info_hash = get_reply.get_info_hash
  end
  
  def update_challenge_number
    send_request A2A_SERVERQUERY_GETCHALLENGE_RequestPacket.new
    @challenge_number = get_reply.get_challenge_number    
  end
 
  def update_ping
    send_request A2A_PING_RequestPacket.new
    start_time = Time.now
    get_reply
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
    p @info_hash["mod_info"]
    
    return_string = ""
    
    return_string << "Ping: #{@ping}\n"
    return_string << "Challenge number: #{@challenge_number}\n"
    
    if @info_hash != nil
      return_string << "Info:\n"
      @info_hash.each do |key, value|
        return_string << "  #{key}: #{value.inspect}\n"
      end
    end
    
    if @player_array != nil
      return_string << "Players:\n"
      @player_array.each do |player|
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

  private

  def get_reply
    @socket.get_reply
  end
  
  def send_request packet
    @socket.send packet
  end
end
