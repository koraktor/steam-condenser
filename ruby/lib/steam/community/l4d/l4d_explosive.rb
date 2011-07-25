# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'steam/community/game_weapon'

# This class represents the statistics of a single explosive weapon for a user
# in Left4Dead
#
# @author Sebastian Staudt
class L4DExplosive

  include GameWeapon

  # Creates a new instance of an explosivve based on the given XML data
  #
  # @param [REXML::Element] weapon_data The XML data of this explosive
  def initialize(weapon_data)
    super weapon_data

    @id    = weapon_data.name
    @shots = weapon_data.elements['thrown'].text.to_i
  end

  # Returns the average number of killed zombies for one shot of this explosive
  #
  # @return [Float] The average number of kills per shot
  def avg_kills_per_shot
    1 / avg_shots_per_kill
  end

end
