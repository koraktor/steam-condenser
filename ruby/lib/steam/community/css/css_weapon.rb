# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

# Represents the stats for a Counter-Strike: Source weapon for a specific user
#
# @author Sebastian Staudt
class CSSWeapon

  # Returns the number of hits achieved with this weapon
  #
  # @return [Fixnum] The number of hits achieved
  attr_reader :hits

  # Returns the name of this weapon
  #
  # @return [String] The name of this weapon
  attr_reader :name

  # Returns the number of kills achieved with this weapon
  #
  # @return [Fixnum] The number of kills achieved
  attr_reader :kills

  # Returns the number of shots fired with this weapon
  #
  # @return [Fixnum] The number of shots fired
  attr_reader :shots

  # Creates a new instance of a Counter-Strike: Source weapon based on the
  # given XML data
  #
  # @param [String] weapon_name The name of the weapon
  # @param [REXML::Element] weapons_data The XML data of all weapons
  def initialize(weapon_name, weapons_data)
    @name     = weapon_name

    @favorite = (weapons_data.elements['favorite'].text == @name)
    @kills    = weapons_data.elements["#{@name}_kills"].text.to_i

    if @name != 'grenade' && @name != 'knife'
      @hits     = weapons_data.elements["#{@name}_hits"].text.to_i
      @shots    = weapons_data.elements["#{@name}_shots"].text.to_i
    end
  end

  # Returns the accuracy of this player with this weapon
  #
  # @return [Float] The accuracy with this weapon
  def accuracy
    (@shots > 0) ? @hits.to_f / @shots : 0
  end

  # Returns whether this weapon is the favorite weapon of this player
  #
  # @return [Boolean] `true` if this is the favorite weapon
  def favorite?
    @favorite
  end

  # Returns the kill-shot-ratio of this player with this weapon
  #
  # @return [Float] The kill-shot-ratio
  def ksratio
    (@shots > 0) ? @kills.to_f / @shots : 0
  end

end
