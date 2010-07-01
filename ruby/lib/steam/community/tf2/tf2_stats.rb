# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/community/tf2/tf2_class_factory'

# The TF2Stats class represents the game statistics for a single user in Team
# Fortress 2
class TF2Stats < GameStats

  attr_reader :accumulated_points

  # Creates a TF2Stats object by calling the super constructor with the game
  # name "tf2"
  def initialize(steam_id)
    super steam_id, 'tf2'

    if public?
      @accumulated_points = @xml_data.elements['stats/accumulatedPoints'].text.to_i
    end
  end

  # Returns a Hash of TF2Class for this user containing all Team Fortress 2
  # classes. If the classes haven't been parsed already, parsing is done now.
  def class_stats
    return unless public?

    if @class_stats.nil?
      @class_stats = Hash.new
      @xml_data.elements.each('stats/classData') do |class_data|
        @class_stats[class_data.elements['className'].text] = TF2ClassFactory.tf2_class(class_data)
      end
    end

    @class_stats
  end

end
