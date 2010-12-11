# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

# AlienSwarmWeapon holds statistical information about weapons used by a player
# in Alien Swarm.
class AlienSwarmWeapon

  include GameWeapon

  attr_reader :accuracy, :damage, :friendly_fire, :name, :shots

  # Creates a new instance of AlienSwarmWeapon based on the assigned weapon
  # name and XML data
  def initialize(weapon_data)
    super

    @accuracy      = weapon_data.elements['accuracy'].text
    @damage        = weapon_data.elements['damage'].text
    @friendly_fire = weapon_data.elements['friendlyfire'].text.to_i
    @name          = weapon_data.elements['name'].text
    @shots         = weapon_data.elements['shotsfired'].text
  end

end
