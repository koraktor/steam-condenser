# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/community/tf2/tf2_class'

# Represents the stats for the Team Fortress 2 Medic class for a specific user
#
# @author Sebastian Staudt
class TF2Medic

  include TF2Class

  # Returns the maximum health healed for teammates by the player in a
  # single life as a Medic
  #
  # @return [Fixnum] Maximum health healed
  attr_reader :max_health_healed

  # Returns the maximum number of ÜberCharges provided by the player in a
  # single life as a Medic
  #
  # @return [Fixnum] Maximum number of ÜberCharges
  attr_reader :max_ubercharges

  # Creates a new instance of the Medic class based on the given XML data
  #
  # @param [REXML::Element] class_data The XML data for this Medic
  def initialize(class_data)
    super class_data

    @max_health_healed = class_data.elements['ihealthpointshealed'].text.to_i
    @max_ubercharges   = class_data.elements['inuminvulnerable'].text.to_i
  end

end
