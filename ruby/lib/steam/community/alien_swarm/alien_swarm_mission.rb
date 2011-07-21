# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

# This class holds statistical information about missions played by a player in
# Alien Swarm
#
# @author Sebastian Staudt
class AlienSwarmMission

  # Returns the avarage damage taken by the player while playing a round in
  # this mission
  #
  # @return [Float] The average damage taken by the player
  attr_reader :avg_damage_taken

  # Returns the avarage damage dealt by the player to team mates while playing
  # a round in this mission
  #
  # @return [Float] The average damage dealt by the player to team mates
  attr_reader :avg_friendly_fire

  # Returns the avarage number of aliens killed by the player while playing a
  # round in this mission
  #
  # @return [Float] The avarage number of aliens killed by the player
  attr_reader :avg_kills

  # Returns the highest difficulty the player has beat this mission in
  #
  # @return [String] The highest difficulty the player has beat this mission in
  attr_reader :best_difficulty

  # Returns the total damage taken by the player in this mission
  #
  # @return [Fixnum] The total damage taken by the player
  attr_reader :damage_taken

  # Returns the total damage dealt by the player to team mates in this mission
  #
  # @return [Fixnum] The total damage dealt by the player to team mates
  attr_reader :friendly_fire

  # Returns the number of successful rounds the player played in this mission
  #
  # @return [Fixnum] The number of successful rounds of this mission
  attr_reader :games_successful

  # Returns the URL to a image displaying the mission
  #
  # @return [String] The URL of the mission's image
  attr_reader :img

  # Returns the total number of aliens killed by the player in this mission
  #
  # @return [Fixnum] The total number of aliens killed by the player
  attr_reader :kills

  # Returns the file name of the mission's map
  #
  # @return [String] The file name of the mission's map
  attr_reader :map_name

  # Returns the name of the mission
  #
  # @return [String] The name of the mission
  attr_reader :name

  # Returns various statistics about the times needed to accomplish this
  # mission
  #
  # This includes the best times for each difficulty, the average time and the
  # total time spent in this mission.
  #
  # @return [Hash<Symbol, String>] Various time statistics about this mission
  attr_reader :time

  # Returns the number of games played in this mission
  #
  # @return [Fixnum] The number of games played in this mission
  attr_reader :total_games

  # Returns the percentage of successful games played in this mission
  #
  # @return [Float] The percentage of successful games played in this mission
  attr_reader :total_games_percentage

  # Creates a new mission instance of based on the given XML data
  #
  # @param [REXML::Element] mission_data The data representing this mission
  def initialize(mission_data)
    @avg_damage_taken       = mission_data.elements['damagetakenavg'].text.to_f
    @avg_friendly_fire      = mission_data.elements['friendlyfireavg'].text.to_f
    @avg_kills              = mission_data.elements['killsavg'].text.to_f
    @best_difficulty        = mission_data.elements['bestdifficulty'].text
    @damage_taken           = mission_data.elements['damagetaken'].text.to_i
    @friendly_fire          = mission_data.elements['friendlyfire'].text.to_i
    @games_successful       = mission_data.elements['gamessuccess'].text.to_i
    @img                    = AlienSwarmStats::BASE_URL + mission_data.elements['image'].text
    @kills                  = mission_data.elements['kills'].text.to_i
    @map_name               = mission_data.name
    @name                   = mission_data.elements['name'].text
    @total_games            = mission_data.elements['gamestotal'].text.to_i
    @total_games_percentage = mission_data.elements['gamestotalpct'].text.to_f

    @time = {}
    @time[:average] = mission_data.elements['avgtime'].text
    @time[:brutal]  = mission_data.elements['brutaltime'].text
    @time[:easy]    = mission_data.elements['easytime'].text
    @time[:hard]    = mission_data.elements['hardtime'].text
    @time[:insane]  = mission_data.elements['insanetime'].text
    @time[:normal]  = mission_data.elements['normaltime'].text
    @time[:total]   = mission_data.elements['totaltime'].text
  end

end
