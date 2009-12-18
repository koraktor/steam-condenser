# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt

require "steam/community/tf2/tf2_class"

# Represents the stats for the Team Fortress 2 medic class for a specific user
class TF2Medic < TF2Class
  
  attr_reader :max_health_healed, :max_ubercharges
  
  def initialize(class_data)
    super class_data
    
    @max_health_healed = class_data.elements["ihealthpointshealed"].text.to_i
    @max_ubercharges   = class_data.elements["inuminvulnerable"].text.to_i
  end
  
end