# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

# Represents the stats for a Team Fortress 2 class for a specific user
class TF2Class
  
  # Creates a new instance of TF2Class based on the assigned XML data
  def initialize(class_data)
    @class_name              = class_data.elements["className"].text
    @max_buildings_destroyed = class_data.elements["ibuildingsdestroyed"].text.to_i
    @max_captures            = class_data.elements["ipointcaptures"].text.to_i
    @max_damage              = class_data.elements["idamagedealt"].text.to_i
    @max_defenses            = class_data.elements["ipointdefenses"].text.to_i
    @max_dominations         = class_data.elements["idominations"].text.to_i
    @max_killassists         = class_data.elements["ikillassists"].text.to_i
    @max_kills               = class_data.elements["inumberofkills"].text.to_i
    @max_revenges            = class_data.elements["irevenge"].text.to_i
    @max_score               = class_data.elements["ipointsscored"].text.to_i
    @max_time_alive          = class_data.elements["iplaytime"].text.to_i
    @play_time               = class_data.elements["playtimeSeconds"].text.to_i
    
    case @class_name
      when "Engineer" then
        @max_buildings_built = class_data.elements["ibuildingsbuilt"].text.to_i
        @max_teleports       = class_data.elements["inumteleports"].text.to_i
        @max_sentry_kills    = class_data.elements["isentrykills"].text.to_i
      when "Medic" then
        @max_ubercharges     = class_data.elements["inuminvulnerable"].text.to_i
        @max_health_healed   = class_data.elements["ihealthpointshealed"].text.to_i
      when "Sniper" then
        @max_headshots       = class_data.elements["iheadshots"].text.to_i
      when "Spy" then
        @max_backstabs       = class_data.elements["ibackstabs"].text.to_i
        @max_health_leeched  = class_data.elements["ihealthpointsleached"].text.to_i
    end
  end
  
end