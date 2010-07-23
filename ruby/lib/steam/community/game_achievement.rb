# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

# The GameAchievement class represents a specific achievement for a single game
# and for a single user
class GameAchievement

  attr_reader :app_id, :name, :steam_id64, :timestamp

  # Creates the achievement with the given name for the given user and game
  # and achievement data
  def initialize(steam_id64, app_id, achievement_data)
    @app_id     = app_id
    @name       = achievement_data.elements['name'].text
    @steam_id64 = steam_id64
    @unlocked   = (achievement_data.attributes['closed'].to_i == 1)

    if @unlocked && !achievement_data.elements['unlockTimestamp'].nil?
      @timestamp  = Time.at(achievement_data.elements['unlockTimestamp'].text.to_i)
    end
  end

  # Returns whether this achievement has been unlocked by its owner
  def unlocked?
    @unlocked
  end

end
