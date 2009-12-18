# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt

require "steam/community/tf2/tf2_class"

# Represents the stats for the Team Fortress 2 engineer class for a specific
# user
class TF2Engineer < TF2Class
  
  attr_reader :max_buildings_built, :max_teleports, :max_sentry_kills
  
  # Creates a new instance of TF2Engineer based on the assigned XML data
  def initialize(class_data)
    super class_data
    
    @max_buildings_built = class_data.elements["ibuildingsbuilt"].text.to_i
    @max_teleports       = class_data.elements["inumteleports"].text.to_i
    @max_sentry_kills    = class_data.elements["isentrykills"].text.to_i
  end
  
end