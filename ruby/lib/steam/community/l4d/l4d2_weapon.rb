# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'steam/community/l4d/abstract_l4d_weapon'

# This class represents the statistics of a single weapon for a user in
# Left4Dead 2
#
# @author Sebastian Staudt
class L4D2Weapon

  include AbstractL4DWeapon

  # Returns the amount of damage done by the player with this weapon
  #
  # @return [Fixnum] The damage done by this weapon
  attr_reader :damage

  # Returns the weapon group this weapon belongs to
  #
  # @return [String] The group this weapon belongs to
  attr_reader :weapon_group

  # Creates a new instance of a weapon based on the given XML data
  #
  # @param [REXML::Element] weapon_data The XML data of this weapon
  def initialize(weapon_data)
    super weapon_data

    @damage          = weapon_data.elements['damage'].text.to_i
    @kill_percentage = weapon_data.elements['pctkills'].text
    @weapon_group    = weapon_data.attribute('group')
  end

end
