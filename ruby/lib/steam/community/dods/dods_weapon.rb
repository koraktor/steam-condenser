# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

class DoDSWeapon

  attr_reader :headshots, :id, :kills, :name, :shots_fired, :shots_hit

  # Creates a new instance of DoDSWeapon based on the assigned XML data
  def initialize(weapon_data)
    @headshots = weapon_data.elements['headshots'].text.to_i
    @id = weapon_data.attributes['key']
    @kills = weapon_data.elements['kills'].text.to_i
    @name = weapon_data.elements['name'].text
    @shots_fired = weapon_data.elements['shotsfired'].text.to_i
    @shots_hit = weapon_data.elements['shotshit'].text.to_i
  end

  # Returns the average number of hits needed for a kill with this weapon.
  # Calculates the value if needed.
  def avg_hits_per_kill
    @avg_hits_per_kill = @shots_hit.to_f / @kills if @avg_hits_per_kill.nil?
  end

  # Returns the average number of shots needed for a kill with this weapon.
  # Calculates the value if needed.
  def avg_shots_per_kill
    @avg_shots_per_kill = @shots_fired.to_f / @kills if @avg_shots_per_kill.nil?
  end

  # Returns the percentage of hits relative to the shots fired with this
  # weapon. Calculates the value if needed.
  def hit_percentage
    @hit_percentage = @shots_hit.to_f / @shots_fired if @hit_percentage.nil?
  end

  # Returns the percentage of headshots relative to the shots hit with this
  # weapon. Calculates the value if needed.
  def headshot_percentage
    @headshot_percentage = @headshots.to_f / @shots_hit if @headshot_percentage.nil?
  end

end
