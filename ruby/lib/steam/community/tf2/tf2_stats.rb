# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/community/tf2/tf2_class"

# The TF2Stats class represents the game statistics for a single user in Team
# Fortress 2
class TF2Stats < GameStats
  
  # Creates a TF2Stats object by calling the super constructor with the game
  # name "TF2"
  def initialize(steam_id)
    super steam_id, "TF2"
  end
  
  # Returns a Hash of TF2Class for this user containing all Team Fortress 2
  # classes. If the classes haven't been parsed already, parsing is done now.
  def class_stats
    if @class_stats.nil?
      @class_stats = Hash.new
      @xml_data.elements["stats"].elements.each("classData") do |class_data|
        @class_stats[class_data.elements["className"].text] = TF2Class.new class_data
      end
    end
    
    return @class_stats
  end
  
end