# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'steam/community/game_weapon'

# This class holds statistical information about weapons used by a player
# in Alien Swarm
#
# @author Sebastian Staudt
class AlienSwarmWeapon

  include GameWeapon

  # Returns the accuracy of the player with this weapon
  #
  # @return [Float] The accuracy of the player with this weapon
  attr_reader :accuracy

  # Returns the damage achieved with this weapon
  #
  # @return [Fixnum] The damage achieved with this weapon
  attr_reader :damage

  # Returns the damage dealt to team mates with this weapon
  #
  # @return [Fixnum] The damage dealt to team mates with this weapon
  attr_reader :friendly_fire

  # Returns the name of this weapon
  #
  # @return [String] The name of this weapon
  attr_reader :name

  # Creates a new weapon instance based on the assigned weapon XML data
  #
  # @param [REXML::Element] weapon_data The data representing this weapon
  def initialize(weapon_data)
    super

    @accuracy      = weapon_data.elements['accuracy'].text.to_f
    @damage        = weapon_data.elements['damage'].text.to_i
    @friendly_fire = weapon_data.elements['friendlyfire'].text.to_i
    @name          = weapon_data.elements['name'].text
    @shots         = weapon_data.elements['shotsfired'].text.to_i
  end

end
