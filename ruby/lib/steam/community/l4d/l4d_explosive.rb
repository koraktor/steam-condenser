# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

require 'steam/community/game_weapon'

class L4DExplosive < GameWeapon

  # Creates a new instance of L4DExplosive based on the assigned XML data
  def initialize(weapon_data)
    super weapon_data

    @id                   = weapon_data.name
    @shots                = weapon_data.elements['thrown'].text.to_i
  end

  # Returns the average number of kills for one shot of this explosive.
  def avg_kills_per_shot
    1 / avg_shots_per_kill
  end

end
