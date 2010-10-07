# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/community/tf2/tf2_class'

# Represents the stats for the Team Fortress 2 spy class for a specific user
class TF2Spy

  include TF2Class

  attr_reader :max_backstabs, :max_health_leeched

  # Creates a new instance of TF2Spy based on the assigned XML data
  def initialize(class_data)
    super class_data

    @max_backstabs      = class_data.elements['ibackstabs'].text.to_i
    @max_health_leeched = class_data.elements['ihealthpointsleached'].text.to_i
  end

end
