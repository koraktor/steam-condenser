# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

# The SteamPlayer class represents a player connected to a server
class SteamPlayer

  attr_reader :connect_time, :id, :name, :score
  
  # Creates a new SteamPlayer object based on the given information
  def initialize(id, name, score, connect_time)
    @connect_time = connect_time
    @id = id
    @name = name
    @score = score
  end

  # Returns a String representation of this player
  def to_s
    return "#{@id} \"#{@name}\", Score: #{@score}, Time: #{@connect_time}"
  end
  
end
