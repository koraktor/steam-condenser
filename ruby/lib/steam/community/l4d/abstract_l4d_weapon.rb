# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009-2010, Sebastian Staudt

require 'steam/community/game_weapon'

module AbstractL4DWeapon

  include GameWeapon

  attr_reader :accuracy, :headshots_percentage, :kill_percentage

  # Abstract base constructor which parses common data for both, L4DWeapon and
  # L4D2Weapon
  def initialize(weapon_data)
    super weapon_data

    @accuracy             = weapon_data.elements['accuracy'].text
    @headshots_percentage = weapon_data.elements['headshots'].text
    @id                   = weapon_data.name
    @shots                = weapon_data.elements['shots'].text.to_i
  end

end
