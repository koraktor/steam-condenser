# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

# The SteamPlayer class represents a player connected to a server
class SteamPlayer

  attr_reader :client_port, :connect_time, :id, :ip_address, :name, :loss,
              :ping, :rate, :real_id, :score, :state, :steam_id

  # Creates a new SteamPlayer object based on the given information
  def initialize(id, name, score, connect_time)
    @connect_time = connect_time
    @id = id
    @name = name
    @score = score
    @extended = false
  end

  # Extends this player object with additional information acquired using RCON
  # status
  def add_info(player_data)
    unless player_data[:name] == @name
      raise SteamCondenserException.new('Information to add belongs to a different player.')
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
  def bot?
    @steam_id == 'BOT'
  end

  # Returns whether this player object has extended information
  def extended?
    @extended
  end

  # Returns a String representation of this player
  def to_s
    if @extended
      "\##{@real_id} \"#{@name}\", SteamID: #{@steam_id}, Score: #{@score}, Time: #{@connect_time}"
    else
      "\##{@id} \"#{@name}\", Score: #{@score}, Time: #{@connect_time}"
    end
  end

end
