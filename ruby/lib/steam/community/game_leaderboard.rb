# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/game_leaderboard_entry';
require 'steam/community/steam_id';

# The GameLeaderboard class represents a single leaderboard for a specific game
#
# @author Sebastian Staudt
class GameLeaderboard

  LEADERBOARD_DISPLAY_TYPE_NONE         = 0
  LEADERBOARD_DISPLAY_TYPE_NUMERIC      = 1
  LEADERBOARD_DISPLAY_TYPE_SECONDS      = 2
  LEADERBOARD_DISPLAY_TYPE_MILLISECONDS = 3

  LEADERBOARD_SORT_METHOD_NONE = 0
  LEADERBOARD_SORT_METHOD_ASC  = 1
  LEADERBOARD_SORT_METHOD_DESC = 2

  @@leaderboards = {}

  # Returns the display type of the scores on this leaderboard
  #
  # @return [Fixnum] The display type of the scores
  attr_reader :display_type

  # Returns the number of entries on this leaderboard
  #
  # @return [Fixnum] The number of entries on this leaderboard
  attr_reader :entry_count

  # Returns the ID of the leaderboard
  #
  # @return [Fixnum] The ID of the leaderboard
  attr_reader :id

  # Returns the name of the leaderboard
  #
  # @return [String] The name of the leaderboard
  attr_reader :name

  # Returns the method that is used to sort the entries on the leaderboard
  #
  # @return [Fixnum] The sort method
  attr_reader :sort_method

  # Returns the leaderboard for the given game and leaderboard ID or name
  #
  # @param [String] game_name The short name of the game
  # @param [Fixnum, String] id The ID or name of the leaderboard to return
  # @return [GameLeaderboard] The matching leaderboard if available
  def self.leaderboard(game_name, id)
    leaderboards = self.leaderboards game_name

    if id.is_a? Fixnum
      leaderboards[id]
    else
      leaderboards.each_value do |board|
        return board if board.name == id
      end
    end
  end

  # Returns an array containing all of a game's leaderboards
  #
  # @param [String] game_name The name of the game
  # @return [Array<GameLeaderboard>] The leaderboards for this game
  def self.leaderboards(game_name)
      unless @@leaderboards.key? game_name
        self.load_game_leaderboards game_name
      end

    @@leaderboards[game_name]
  end

  # Returns the entry on this leaderboard for the user with the given SteamID
  #
  # @param [Fixnum, SteamId] steam_id The 64bit SteamID or the `SteamId` object
  #        of the user
  # @return [GameLeaderboardEntry] The entry of the user if available
  # raise [SteamCondenserException] if an error occurs while fetching the
  #         leaderboard
  def entry_for_steam_id(steam_id)
    steam_id = steam_id.steam_id64 if steam_id.is_a? SteamId

    url = "#{@url}&steamid=#{steam_id}"
    xml = REXML::Document.new(open(url, {:proxy => true}).read).root

    error = @xml_data.elements['error']
    raise SteamCondenserError, error.text unless error.nil?

    boards_data.elements.each('entries/entry') do |entry_data|
      if entry_data.elements['steamid'].text.to_i == steam_id
        return GameLeaderboardEntry.new entry_data, self
      end
    end

    nil
  end

  # Returns an array of entries on this leaderboard for the user with the given
  # SteamID and his/her friends
  #
  # @param [Fixnum, SteamId] steam_id The 64bit SteamID or the `SteamId` object
  #        of the user
  # @return [Array<GameLeaderboardEntry>] The entries of the user and his/her
  #         friends
  # raise [SteamCondenserException] if an error occurs while fetching the
  #         leaderboard
  def entry_for_steam_id_friends(steam_id)
    steam_id = steam_id.steam_id64 if steam_id.is_a? SteamId

    url = "#{@url}&steamid=#{steam_id}"
    xml = REXML::Document.new(open(url, {:proxy => true}).read).root

    error = @xml_data.elements['error']
    raise SteamCondenserError, error.text unless error.nil?

    entries = []
    boards_data.elements.each('entries/entry') do |entry_data|
      rank = entry_data.elements['rank'].text.to_i
      entries[rank] = GameLeaderboardEntry.new entry_data, self
    end

    entries
  end

  # Returns the entries on this leaderboard for a given rank range
  #
  # The range is inclusive and a maximum of 5001 entries can be returned in a
  # single request.
  #
  # @param [Fixnum] first The first entry to return from the leaderboard
  # @param [Fixnum] last The last entry to return from the leaderboard
  # @return [Array<GameLeaderboardEntry>] The entries that match the given rank
  #         range
  # raise [SteamCondenserException] if an error occurs while fetching the
  #         leaderboard
  def entry_range(first, last)
    if last < first
      raise SteamCondenserException,
        'First entry must be prior to last entry for leaderboard entry lookup.'
    end

    if (last - first) > 5000
      raise SteamCondenserException,
        'Leaderboard entry lookup is currently limited to a maximum of 5001 entries per request.'
    end

    url = "#{@url}&start=#{first}&end=#{last}"
    xml = REXML::Document.new(open(url, {:proxy => true}).read).root

    error = @xml_data.elements['error']
    raise SteamCondenserError, error.text unless error.nil?

    entries = []
    boards_data.elements.each('entries/entry') do |entry_data|
      rank = entry_data.elements['rank'].text.to_i
      entries[rank] = GameLeaderboardEntry.new entry_data, self
    end

    entries
  end

  private

  # Creates a new leaderboard instance with the given XML data
  #
  # @param [REXML::Element] board_data The XML data of the leaderboard
  def initialize(board_data)
    @url          = board_data.elements['url'].text
    @id           = board_data.elements['lbid'].text.to_i
    @name         = board_data.elements['name'].text
    @entry_count  = board_data.elements['entries'].text.to_i
    @sort_method  = board_data.elements['sortmethod'].text.to_i
    @display_type = board_data.elements['displaytype'].text.to_i
  end

  # Loads the leaderboards of the specified games into the cache
  #
  # @param [String] game_name The short name of the game
  # @raise [SteamCondenserException] if an error occurs while fetching the
  #         leaderboards
  def self.load_leaderboards(game_ame)
    url = "http://steamcommunity.com/stats/#{game_name}/leaderboards/?xml=1"
    boards_data = REXML::Document.new(open(url, {:proxy => true}).read).root

    error = @xml_data.elements['error']
    raise SteamCondenserError, error.text unless error.nil?

    @@leaderboards[game_name] = []
    boards_data.elements.each('leaderboard') do |board_data|
      leaderboard = GameLeaderboard.new board_data
      @@leaderboards[game_name][leaderboard.id] = leaderboard
    end
  end

end
