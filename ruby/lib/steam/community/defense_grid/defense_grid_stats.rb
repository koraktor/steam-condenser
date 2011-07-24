# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'steam/community/game_stats'

# This class represents the game statistics for a single user in Defense Grid:
# The Awakening
#
# @author Sebastian Staudt
class DefenseGridStats < GameStats

  # Returns the
  #
  # @return []
  attr_reader :bronze_medals

  # Returns the
  #
  # @return []
  attr_reader :damage_done

  # Returns the
  #
  # @return []
  attr_reader :damage_campaign

  # Returns the
  #
  # @return []
  attr_reader :damage_challenge

  # Returns the
  #
  # @return []
  attr_reader :encountered

  # Returns the
  #
  # @return []
  attr_reader :gold_medals

  # Returns the
  #
  # @return []
  attr_reader :heat_damage

  # Returns the
  #
  # @return []
  attr_reader :interest

  # Returns the
  #
  # @return []
  attr_reader :killed

  # Returns the
  #
  # @return []
  attr_reader :killed_campaign

  # Returns the
  #
  # @return []
  attr_reader :killed_challenge

  # Returns the
  #
  # @return []
  attr_reader :levels_played

  # Returns the
  #
  # @return []
  attr_reader :levels_played_campaign

  # Returns the
  #
  # @return []
  attr_reader :levels_played_challenge

  # Returns the
  #
  # @return []
  attr_reader :levels_won

  # Returns the
  #
  # @return []
  attr_reader :levels_won_campaign

  # Returns the
  #
  # @return []
  attr_reader :levels_won_challenge

  # Returns the
  #
  # @return []
  attr_reader :orbital_laser_fired

  # Returns the
  #
  # @return []
  attr_reader :orbital_laser_damage

  # Returns the
  #
  # @return []
  attr_reader :resources

  # Returns the
  #
  # @return []
  attr_reader :silver_medals

  # Returns the
  #
  # @return []
  attr_reader :time_played

  # Creates a `DefenseGridStats` instance by calling the super constructor with
  # the game name `'defensegrid:awakening'`
  #
  # @param [String, Fixnum] steam_id The custom URL or the 64bit Steam ID of
  #        the user
  def initialize(steam_id)
    super(steam_id, 'defensegrid:awakening')

    if public?
      general_data = @xml_data.elements['stats/general']

      @bronze_medals           = general_data.elements['bronze_medals_won/value'].text.to_i
      @silver_medals           = general_data.elements['silver_medals_won/value'].text.to_i
      @gold_medals             = general_data.elements['gold_medals_won/value'].text.to_i
      @levels_played           = general_data.elements['levels_played_total/value'].text.to_i
      @levels_played_campaign  = general_data.elements['levels_played_campaign/value'].text.to_i
      @levels_played_challenge = general_data.elements['levels_played_challenge/value'].text.to_i
      @levels_won              = general_data.elements['levels_won_total/value'].text.to_i
      @levels_won_campaign     = general_data.elements['levels_won_campaign/value'].text.to_i
      @levels_won_challenge    = general_data.elements['levels_won_challenge/value'].text.to_i
      @encountered             = general_data.elements['total_aliens_encountered/value'].text.to_i
      @killed                  = general_data.elements['total_aliens_killed/value'].text.to_i
      @killed_campaign         = general_data.elements['total_aliens_killed_campaign/value'].text.to_i
      @killed_challenge        = general_data.elements['total_aliens_killed_challenge/value'].text.to_i
      @resources               = general_data.elements['resources_recovered/value'].text.to_i
      @heat_damage             = general_data.elements['heatdamage/value'].text.to_f
      @time_played             = general_data.elements['time_played/value'].text.to_f
      @interest                = general_data.elements['interest_gained/value'].text.to_f
      @damage_done             = general_data.elements['tower_damage_total/value'].text.to_f
      @damage_campaign         = general_data.elements['tower_damage_total_campaign/value'].text.to_f
      @damage_challenge        = general_data.elements['tower_damage_total_challenge/value'].text.to_f
      @orbital_laser_fired     = @xml_data.elements['stats/orbitallaser/fired/value'].text.to_i
      @orbital_laser_damage    = @xml_data.elements['stats/orbitallaser/damage/value'].text.to_f
    end
  end

  # Returns stats about the aliens encountered by the player
  #
  # The Hash returned uses the names of the aliens as keys. Every value of the
  # Hash is an Array containing the number of aliens encountered as the first
  # element and the number of aliens killed as the second element.
  #
  # @return [Hash<String, Array<Fixnum>>] Stats about the aliens encountered
  def alien_stats
    return unless public?

    if @alien_stats.nil?
      alien_data = @xml_data.elements['stats/aliens']
      @alien_stats = {}
      aliens = %w{swarmer juggernaut crasher spire grunt bulwark drone manta dart
                  decoy rumbler seeker turtle walker racer stealth}

      aliens.each do |alien|
        @alien_stats[alien] = [
          alien_data.elements["#{alien}/encountered/value"].text.to_i,
          alien_data.elements["#{alien}/killed/value"].text.to_i
        ]
      end
    end

    @alien_stats
  end

  # Returns stats about the towers built by the player
  #
  # The Hash returned uses the names of the towers as keys. Every value of
  # the Hash is another Hash using the keys 1 to 3 for different tower levels.
  # The values of these Hash is an Array containing the number of towers built
  # as the first element and the damage dealt by this specific tower type as the
  # second element.
  #
  # The Command tower uses the resources gained as second element.
  # The Temporal tower doesn't have a second element.
  #
  # @return [Hash<String, Array<Fixnum>>] Stats about the towers built
  def tower_stats
    return unless public?

    if @tower_stats.nil?
      tower_data = @xml_data.elements['stats/towers']
      @tower_stats = {}
      towers = %w{cannon flak gun inferno laser meteor missile tesla}

      towers.each do |tower|
        @tower_stats[tower] = {}
        (1..3).each do |i|
          @tower_stats[tower][i] = [
            tower_data.elements["#{tower}[@level=#{i}]/built/value"].text.to_i,
            tower_data.elements["#{tower}[@level=#{i}]/damage/value"].text.to_f
          ]
        end
      end

      @tower_stats['command'] = {}
      (1..3).each do |i|
        @tower_stats['command'][i] = [
          tower_data.elements["command[@level=#{i}]/built/value"].text.to_i,
          tower_data.elements["command[@level=#{i}]/resource/value"].text.to_f
        ]
      end

      @tower_stats['temporal'] = {}
      (1..3).each do |i|
        @tower_stats['temporal'][i] = [
          tower_data.elements["temporal[@level=#{i}]/built/value"].text.to_i,
        ]
      end
    end

    @tower_stats
  end

end
