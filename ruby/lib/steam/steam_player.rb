# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

# The SteamPlayer class represents a player connected to a server
class SteamPlayer

  attr_reader :client_port, :connect_time, :id, :ip_address, :name, :loss,
              :ping, :real_id, :score, :state, :steam_id

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
  def add_info(real_id, name, steam_id, *player_data)
    @extended = true

    raise SteamCondenserException.new('Information to add belongs to a different player.') unless name == @name

    @real_id  = real_id
    @steam_id = steam_id
    if steam_id == 'BOT'
      @state = player_data[0]
    else
      @ip_address, @client_port  = player_data[4].split(':')
      @loss  = player_data[2]
      @ping  = player_data[1]
      @state = player_data[3]
    end
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
