# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

# A module implementing basic functionality for classes representing game
# weapons
#
# @author Sebastian Staudt
module GameWeapon

  # Returns the number of kills achieved with this weapon
  #
  # @return [Fixnum] The number of kills achieved
  attr_reader :kills

  # Returns the unique identifier for this weapon
  #
  # @return [String] The identifier of this weapon
  attr_reader :id

  # Returns the number of shots fired with this weapon
  #
  # @return [Fixnum] The number of shots fired
  attr_reader :shots

  # Creates a new game weapon instance with the data provided
  #
  # @param [REXML::Element] weapon_data The data representing this weapon
  def initialize(weapon_data)
    @kills = weapon_data.elements['kills'].text.to_i
  end

  # Returns the average number of shots needed for a kill with this weapon
  #
  # @return [Float] The average number of shots needed for a kill
  def avg_shots_per_kill
    @shots.to_f / @kills
  end

end
