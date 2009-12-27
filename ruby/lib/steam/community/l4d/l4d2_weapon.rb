# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

require 'steam/community/l4d/abstract_l4d_weapon'

class L4D2Weapon < AbstractL4DWeapon

  attr_reader :damage, :weapon_group

  # Creates a new instance of L4D2Weapon based on the assigned XML data
  def initialize(weapon_data)
    super weapon_data

    @damage          = weapon_data.elements['damage'].text.to_i
    @kill_percentage = weapon_data.elements['pctkills'].text
    @weapon_group    = weapon_data.attribute('group')
  end

end
