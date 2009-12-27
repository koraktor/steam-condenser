# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

require 'abstract_class'
require 'steam/community/game_weapon'

class AbstractL4DWeapon < GameWeapon

  include AbstractClass

  attr_reader :accuracy, :headshots_percentage, :kill_percentage

  # Creates a new instance of L4DWeapon based on the assigned XML data
  def initialize(weapon_data)
    super weapon_data

    @accuracy             = weapon_data.elements['accuracy'].text
    @headshots_percentage = weapon_data.elements['headshots'].text
    @id                   = weapon_data.name
    @shots                = weapon_data.elements['shots'].text.to_i
  end

end
