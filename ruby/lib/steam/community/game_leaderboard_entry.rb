# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

# The GameLeaderboard class represents a single entry in a leaderboard
#
# @author Sebastian Staudt
class GameLeaderboardEntry

  # Returns the Steam ID of this entry's player
  #
  # @return [SteamId] The Steam ID of the player
  attr_reader :steam_id

  # Returns the score of this entry
  #
  # @return [Fixnum] The score of this player
  attr_reader :score

  # Returns the rank where this entry is listed in the leaderboard
  #
  # @return [Fixnum] The rank of this entry
  attr_reader :rank

  # Returns the leaderboard this entry belongs to
  #
  # @return [GameLeaderboard] The leaderboard of this entry
  attr_reader :leaderboard

  # Creates new entry instance for the given XML data and leaderboard
  #
  # @param [REXML::Element] entry_data The XML data of the leaderboard of the
  #        leaderboard entry
  # @param [GameLeaderboard] leaderboard The leaderboard this entry belongs to
  def initialize(entry_data, leaderboard)
    @steam_id    = SteamId.new entry_data.elements['steamid'].text.to_i, false
    @score       = entry_data.elements['score'].text.to_i
    @rank        = entry_data.elements['rank'].text.to_i
    @leaderboard = leaderboard
  end

end
