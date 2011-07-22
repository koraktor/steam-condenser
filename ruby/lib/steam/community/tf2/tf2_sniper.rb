# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/community/tf2/tf2_class'

# Represents the stats for the Team Fortress 2 Sniper class for a specific user
#
# @author Sebastian Staudt
class TF2Sniper

  include TF2Class

  # Returns the maximum number enemies killed with a headshot by the player in
  # a single life as a Sniper
  #
  # @return [Fixnum] Maximum number of headshots
  attr_reader :max_headshots

  # Creates a new instance of the Sniper class based on the given XML data
  #
  # @param [REXML::Element] class_data The XML data for this Sniper
  def initialize(class_data)
    super class_data

    @max_headshots = class_data.elements['iheadshots'].text.to_i
  end

end
