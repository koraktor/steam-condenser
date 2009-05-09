# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require 'steam/community/game_class'

# Represents the stats for a Team Fortress 2 class for a specific user
class TF2Class < GameClass
  
  attr_reader :max_buildings_destroyed, :max_captures, :max_damage,
              :max_defenses, :max_dominations, :max_kill_assists, :max_kills,
              :max_revenges, :max_score, :max_time_alive
  
  # Creates a new instance of TF2Class based on the assigned XML data
  def initialize(class_data)
    @name                    = class_data.elements["className"].text
    @max_buildings_destroyed = class_data.elements["ibuildingsdestroyed"].text.to_i
    @max_captures            = class_data.elements["ipointcaptures"].text.to_i
    @max_damage              = class_data.elements["idamagedealt"].text.to_i
    @max_defenses            = class_data.elements["ipointdefenses"].text.to_i
    @max_dominations         = class_data.elements["idominations"].text.to_i
    @max_kill_assists        = class_data.elements["ikillassists"].text.to_i
    @max_kills               = class_data.elements["inumberofkills"].text.to_i
    @max_revenges            = class_data.elements["irevenge"].text.to_i
    @max_score               = class_data.elements["ipointsscored"].text.to_i
    @max_time_alive          = class_data.elements["iplaytime"].text.to_i
    @play_time               = class_data.elements["playtimeSeconds"].text.to_i
  end
  
end