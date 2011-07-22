# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/community/tf2/tf2_class'

# Represents the stats for the Team Fortress 2 Engineer class for a specific
# user
#
# @author Sebastian Staudt
class TF2Engineer

  include TF2Class

  # Returns the maximum number of buildings built by the player in a single
  # life as an Engineer
  #
  # @return [Fixnum] Maximum number of buildings built
  attr_reader :max_buildings_built

  # Returns the maximum number of enemies killed by sentry guns built by the
  # player in a single life as an Engineer
  #
  # @return [Fixnum] Maximum number of sentry kills
  attr_reader :max_sentry_kills

  # Returns the maximum number of teammates teleported by teleporters built by
  # the player in a single life as an Engineer
  #
  # @return [Fixnum] Maximum number of teleports
  attr_reader :max_teleports

  # Creates a new instance of the Engineer class based on the given XML data
  #
  # @param [REXML::Element] class_data The XML data for this Engineer
  def initialize(class_data)
    super class_data

    @max_buildings_built = class_data.elements['ibuildingsbuilt'].text.to_i
    @max_teleports       = class_data.elements['inumteleports'].text.to_i
    @max_sentry_kills    = class_data.elements['isentrykills'].text.to_i
  end

end
