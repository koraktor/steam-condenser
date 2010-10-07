# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009-2010, Sebastian Staudt

# Super class for classes representing game weapons
module GameWeapon

  attr_reader :kills, :id, :shots

  def initialize(weapon_data)
    @kills     = weapon_data.elements['kills'].text.to_i
  end

  # Returns the average number of shots needed for a kill with this weapon.
  # Calculates the value if needed.
  def avg_shots_per_kill
    @avg_shots_per_kill = @shots.to_f / @kills if @avg_shots_per_kill.nil?

    @avg_shots_per_kill
  end

end
