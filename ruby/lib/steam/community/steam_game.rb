# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

# This class represents a game available on Steam
#
# @author Sebastian Staudt
class SteamGame

  @@games = {}

  # Returns the Steam application ID of this game
  #
  # @return [Fixnum] The Steam application ID of this game
  attr_reader :app_id

  # Returns the full name of this game
  #
  # @return [String] The full name of this game
  attr_reader :name

  # Returns the short name of this game (also known as "friendly name")
  #
  # @return [String] The short name of this game
  attr_reader :short_name

  # Creates a new or cached instance of the game specified by the given XML
  # data
  #
  # @param [REXML::Element] game_data The XML data of the game
  # @see #initialize
  def self.new(game_data)
    app_id = game_data.elements['appID'].text.to_i
    @@games.key?(app_id) ? @@games[app_id] : super(app_id, game_data)
  end

  # Returns whether this game has statistics available
  #
  # @return [Boolean] `true` if this game has stats
  def has_stats?
    !@short_name.nil?
  end

  # Creates a stats object for the given user and this game
  #
  # @param [String, Fixnum] steam_id The custom URL or the 64bit Steam ID of
  #        the user
  # @return [GameStats] The stats of this game for the given user
  def user_stats(steam_id)
    return unless has_stats?

    GameStats.create_game_stats steam_id, @short_name
  end

  private

  # Creates a new instance of a game with the given data and caches it
  #
  # @param [Fixnum] app_id The application ID of the game
  # @param [REXML::Element] game_data The XML data of the game
  def initialize(app_id, game_data)
    @app_id = app_id
    @name   = game_data.elements['name'].text
    if game_data.elements['globalStatsLink'].nil?
      @short_name = nil
    else
      @short_name = game_data.elements['globalStatsLink'].text.match(/http:\/\/steamcommunity.com\/stats\/([^?\/]+)\/achievements\//)[1].downcase
    end

    @@games[@app_id] = self
  end

end
