# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/community/tf2/tf2_class'
require 'steam/community/tf2/tf2_engineer'
require 'steam/community/tf2/tf2_medic'
require 'steam/community/tf2/tf2_sniper'
require 'steam/community/tf2/tf2_spy'

# The TF2ClassFactory is used to created instances of TF2Class based on the XML
# input data
module TF2ClassFactory

  # Creates a new instance of TF2Class storing the statistics for a Team
  # Fortress 2 class with the assigned XML data
  def self.tf2_class(class_data)
    case class_data.elements['className'].text
      when 'Engineer' then
        return TF2Engineer.new(class_data)
      when 'Medic' then
        return TF2Medic.new(class_data)
      when 'Sniper' then
        return TF2Sniper.new(class_data)
      when 'Spy' then
        return TF2Spy.new(class_data)
      else
        return TF2Class.new(class_data)
    end
  end

end
