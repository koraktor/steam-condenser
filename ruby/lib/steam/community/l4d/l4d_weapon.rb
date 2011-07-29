# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'steam/community/l4d/abstract_l4d_weapon'

# This class represents the statistics of a single weapon for a user in
# Left4Dead
#
# @author Sebastian Staudt
class L4DWeapon

  include AbstractL4DWeapon

  # Creates a new instance of a weapon based on the given XML data
  #
  # @param [REXML::Element] weapon_data The XML data for this weapon
  def initialize(weapon_data)
    super weapon_data

    @kill_percentage = weapon_data.elements['killpct'].text.to_f * 0.01
  end

end
