# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

require 'steam/community/l4d/abstract_l4d_weapon'

class L4DWeapon < AbstractL4DWeapon

  attr_reader :accuracy, :headshots_percentage, :kill_percentage

  # Creates a new instance of L4DWeapon based on the assigned XML data
  def initialize(weapon_data)
    super weapon_data

    @kill_percentage = weapon_data.elements['killpct'].text
  end

end
