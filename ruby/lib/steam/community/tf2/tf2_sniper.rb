# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt

require "steam/community/tf2/tf2_class"

# Represents the stats for the Team Fortress 2 sniper class for a specific user
class TF2Sniper < TF2Class
  
  # Creates a new instance of TF2Sniper based on the assigned XML data
  def initialize(class_data)
    super class_data
    
    @max_headshots = class_data.elements["iheadshots"].text.to_i
  end
  
end