# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

require 'abstract_class'

# Super class for classes representing game weapons
class GameWeapon
  
  include AbstractClass

  attr_reader :headshots, :shots, :id

  def initialize(weapon_data)
    @kills     = weapon_data.elements['kills'].text.to_i
  end

  # Returns the average number of shots needed for a kill with this weapon.
  # Calculates the value if needed.
  def avg_shots_per_kill
    if @avg_shots_per_kill.nil?
      @avg_shots_per_kill = @shots.to_f / @kills
    end

    @avg_shots_per_kill
  end

end
