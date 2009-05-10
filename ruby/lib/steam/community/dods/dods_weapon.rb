# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

require 'steam/community/game_weapon'

class DoDSWeapon < GameWeapon

  attr_reader :headshots, :name, :shots_hit

  # Creates a new instance of DoDSWeapon based on the assigned XML data
  def initialize(weapon_data)
    super weapon_data

    @headshots = weapon_data.elements['headshots'].text.to_i
    @id        = weapon_data.attributes['key']
    @name      = weapon_data.elements['name'].text
    @shots     = weapon_data.elements['shotsfired'].text.to_i
    @shots_hit = weapon_data.elements['shotshit'].text.to_i
  end

  # Returns the average number of hits needed for a kill with this weapon.
  # Calculates the value if needed.
  def avg_hits_per_kill
    if @avg_hits_per_kill.nil?
      @avg_hits_per_kill = @shots_hit.to_f / @kills
    end

    @avg_hits_per_kill
  end

  # Returns the percentage of hits relative to the shots fired with this
  # weapon. Calculates the value if needed.
  def hit_percentage
    if @hit_percentage.nil?
      @hit_percentage = @shots_hit.to_f / @shots_fired
    end

    @hit_percentage
  end

  # Returns the percentage of headshots relative to the shots hit with this
  # weapon. Calculates the value if needed.
  def headshot_percentage
    if @headshot_percentage.nil?
      @headshot_percentage = @headshots.to_f / @shots_hit
    end

    @headshot_percentage
  end

end
