# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

# The SteamPlayer class represents a player connected to a server
#
# @author Sebastian Staudt
class SteamPlayer

  # Returns the client port of this player
  #
  # @return [Fixnum] The client port of the player
  attr_reader :client_port

  # Returns the time this player is connected to the server
  #
  # @return [Float] The connection time of the player
  attr_reader :connect_time

  # Returns the ID of this player
  #
  # @return [Fixnum] The ID of this player
  attr_reader :id

  # Returns the IP address of this player
  #
  # @return [String] The IP address of this player
  attr_reader :ip_address

  # Returns the nickname of this player
  #
  # @return [String] The name of this player
  attr_reader :name

  # Returns the packet loss of this player's connection
  #
  # @return [String] The packet loss of this player's connection
  attr_reader :loss

  # Returns the ping of this player
  #
  # @return [Fixnum] The ping of this player
  attr_reader :ping

  # Returns the rate of this player
  #
  # @return [Fixnum] The rate of this player
  attr_reader :rate

  # Returns the real ID (as used on the server) of this player
  #
  # @return [Fixnum] The real ID of this player
  attr_reader :real_id

  # Returns the score of this player
  #
  # @return [Fixnum] The score of this player
  attr_reader :score

  # Returns the connection state of this player
  #
  # @return [String] The connection state of this player
  attr_reader :state

  # Returns the SteamID of this player
  #
  # @return [String] The SteamID of this player
  attr_reader :steam_id

  # Creates a new player instance with the given information
  #
  # @param [Fixnum] id The ID of the player on the server
  # @param [String] name The name of the player
  # @param [Fixnum] score The score of the player
  # @param [Float] connect_time The time the player is connected to the server
  def initialize(id, name, score, connect_time)
    @connect_time = connect_time
    @id = id
    @name = name
    @score = score
    @extended = false
  end

  # Extends a player object with information retrieved from a RCON call to
  # the status command
  #
  # @param [String] player_data The player data retrieved from `rcon status`
  # @raise [SteamCondenserException] if the information belongs to another
  #         player
  def add_info(player_data)
    unless player_data[:name] == @name
      raise SteamCondenserException, 'Information to add belongs to a different player.'
    end

    @extended = true

    @real_id  = player_data[:userid].to_i
    @steam_id = player_data[:uniqueid]
    @state    = player_data[:state] if player_data.key? :state

    if !bot?
      @loss = player_data[:loss].to_i
      @ping = player_data[:ping].to_i

      if player_data.key? :adr
        @ip_address, @client_port  = player_data[:adr].split(':')
        @client_port = @client_port.to_i
      end

      @rate  = player_data[:rate].to_i if player_data.key? :rate
    end
  end

  # Returns whether this player is a bot
  #
  # @return bool `true` if this player is a bot
  def bot?
    @steam_id == 'BOT'
  end

  # Returns whether this player object has extended information gathered
  # using RCON
  #
  # @return bool `true` if extended information for this player is available
  def extended?
    @extended
  end

  # Returns a string representation of this player
  #
  # @return [String] A string representing this player
  def to_s
    if @extended
      "\##{@real_id} \"#{@name}\", SteamID: #{@steam_id}, Score: #{@score}, Time: #{@connect_time}"
    else
      "\##{@id} \"#{@name}\", Score: #{@score}, Time: #{@connect_time}"
    end
  end

end
