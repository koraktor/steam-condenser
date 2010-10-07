# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009-2010, Sebastian Staudt

require 'steam/community/game_class'

# Represents the stats for a DoD:S class for a specific user
class DoDSClass

  include GameClass

  attr_reader :blocks, :bombs_defused, :bombs_planted, :captures, :deaths,
              :dominations, :key, :kills, :name, :rounds_lost, :rounds_won,
              :revenges

  # Creates a new instance of DoDSClass based on the assigned XML data
  def initialize(class_data)
    @blocks        = class_data.elements['blocks'].text.to_i
    @bombs_defused = class_data.elements['bombsdefused'].text.to_i
    @bombs_planted = class_data.elements['bombsplanted'].text.to_i
    @captures      = class_data.elements['captures'].text.to_i
    @deaths        = class_data.elements['deaths'].text.to_i
    @dominations   = class_data.elements['dominations'].text.to_i
    @key           = class_data.attributes['key']
    @kills         = class_data.elements['kills'].text.to_i
    @name          = class_data.elements["name"].text
    @play_time     = class_data.elements["playtime"].text.to_i
    @rounds_lost   = class_data.elements['roundslost'].text.to_i
    @rounds_won    = class_data.elements['roundswon'].text.to_i
    @revenges      = class_data.elements['revenges'].text.to_i
  end

end
