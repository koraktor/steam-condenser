# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

# CSSWeapon holds statistical information about weapons used by a player in
# Counter-Strike: Source.
class CSSWeapon

  attr_reader :accuracy, :hits, :name, :kills, :ksratio, :shots

  # Creates a new instance of CSSWeapon based on the assigned XML data
  def initialize(weapons_data, weapon_name)
    @name     = weapon_name

    @favorite = (weapons_data.elements['favorite'].text == weapon_name)
    @kills    = weapons_data.elements["#{@name}_kills"].text.to_i

    if @name != 'grenade' && @name != 'knife'
      @hits     = weapons_data.elements["#{@name}_hits"].text.to_i
      @shots    = weapons_data.elements["#{@name}_shots"].text.to_i
      @accuracy = (@shots > 0) ? @hits.to_f / @shots : 0
      @ksratio  = (@shots > 0) ? @kills.to_f / @shots : 0
    end
  end

  # Returns whether this weapon is the favorite weapon of this player
  def favorite?
    @favorite
  end

end
