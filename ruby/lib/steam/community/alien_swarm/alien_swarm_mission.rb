# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

# AlienSwarmMission holds statistical information about missions played by a
# player in Alien Swarm.
class AlienSwarmMission

  attr_reader :avg_damage_taken, :avg_friendly_fire, :avg_kills,
              :best_difficulty, :damage_taken, :friendly_fire,
              :games_successful, :img, :kills, :map_name, :name, :time,
              :total_games, :total_games_percentage

  # Creates a new instance of AlienSwarmMission based on the assigned mission
  # name and XML data
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
