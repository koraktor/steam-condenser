# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'cgi'
require 'open-uri'
require 'rexml/document'

require 'exceptions/steam_condenser_exception'
require 'steam/community/cacheable'
require 'steam/community/game_stats'
require 'steam/community/steam_group'

# The SteamId class represents a Steam Community profile (also called Steam ID)
#
# @author Sebastian Staudt
class SteamId

  include Cacheable
  cacheable_with_ids :custom_url, :steam_id64

  # Returns the custom URL of this Steam ID
  #
  # The custom URL is a user specified unique string that can be used instead
  # of the 64bit SteamID as an identifier for a Steam ID.
  #
  # @note The custom URL is not necessarily the same as the user's nickname.
  # @return [String] The custom URL of this Steam ID
  attr_reader :custom_url

  # Returns the favorite game of this user
  #
  # @deprecated The favorite game is no longer listed for new users
  # @return [String] The favorite game of this user
  attr_reader :favorite_game

  # Returns the number of hours that this user played his/her favorite game in
  # the last two weeks
  #
  # @deprecated The favorite game is no longer listed for new users
  # @return [String] The number of hours the favorite game has been played
  #         recently
  attr_reader :favorite_game_hours_played

  # Returns the groups this user is a member of
  #
  # @return [Array<SteamGroup>] The groups this user is a member of
  attr_reader :groups

  # Returns the headline specified by the user
  #
  # @return [String] The headline specified by the user
  attr_reader :head_line

  # Returns the number of hours that this user played a game in the last two
  # weeks
  #
  # @return [Float] The number of hours the user has played recently
  attr_reader :hours_played

  # Returns the links that this user has added to his/her Steam ID
  #
  # The keys of the hash contain the titles of the links while the values
  # contain the corresponding URLs.
  #
  # @return [Hash<String, String>] The links of this user
  attr_reader :links

  # Returns the location of the user
  #
  # @return [String] The location of the user
  attr_reader :location

  # Returns the date of registration for the Steam account belonging to this
  # SteamID
  #
  # @return [Time] The date of the Steam account registration
  attr_reader :member_since

  # Returns the games this user has played the most in the last two weeks
  #
  # The keys of the hash contain the names of the games while the values
  # contain the number of hours the corresponding game has been played by the
  # user in the last two weeks.
  #
  # @return [Hash<String, Float>] The games this user has played the most
  #         recently
  attr_reader :most_played_games

  # Returns the Steam nickname of the user
  #
  # @return [String] The Steam nickname of the user
  attr_reader :nickname

  # Returns the privacy state of this Steam ID
  #
  # @return [String] The privacy state of this Steam ID
  attr_reader :privacy_state

  # Returns the real name of this user
  #
  # @return [String] The real name of this user
  attr_reader :real_name

  # Returns the message corresponding to this user's online state
  #
  # @return [String] The message corresponding to this user's online state
  # @see #ingame?
  # @see #online?
  attr_reader :state_message

  # Returns this user's 64bit SteamID
  #
  # @return [Fixnum] This user's 64bit SteamID
  attr_reader :steam_id64

  # Returns the Steam rating calculated over the last two weeks' activity
  #
  # @return [Float] The Steam rating of this user
  attr_reader :steam_rating

  # Returns the summary this user has provided
  #
  # @return [String] This user's summary
  attr_reader :summary

  # Returns the visibility state of this Steam ID
  #
  # @return [Fixnum] This Steam ID's visibility State
  attr_reader :visibility_state

  # Converts a 64bit numeric SteamID as used by the Steam Community to a
  # SteamID as reported by game servers
  #
  # @param [Fixnum] community_id The SteamID string as used by the Steam
  #        Community
  # @raise [SteamCondenserException] if the community ID is to small
  # @return [String] The converted SteamID, like `STEAM_0:0:12345`
  def self.convert_community_id_to_steam_id(community_id)
    steam_id1 = community_id % 2
    steam_id2 = community_id - 76561197960265728

    unless steam_id2 > 0
      raise SteamCondenserException, "SteamID #{community_id} is too small."
    end

    steam_id2 = (steam_id2 - steam_id1) / 2

    "STEAM_0:#{steam_id1}:#{steam_id2}"
  end

  # Converts a SteamID as reported by game servers to a 64bit numeric SteamID
  # as used by the Steam Community
  #
  # @param [String] steam_id The SteamID string as used on servers, like
  #        `STEAM_0:0:12345`
  # @raise [SteamCondenserException] if the SteamID doesn't have the correct
  #        format
  # @return [Fixnum] The converted 64bit numeric SteamID
  def self.convert_steam_id_to_community_id(steam_id)
    if steam_id == 'STEAM_ID_LAN' || steam_id == 'BOT'
      raise SteamCondenserException, "Cannot convert SteamID \"#{steam_id}\" to a community ID."
    elsif steam_id.match(/^STEAM_[0-1]:[0-1]:[0-9]+$/).nil?
      raise SteamCondenserException, "SteamID \"#{steam_id}\" doesn't have the correct format."
    end

    steam_id = steam_id[6..-1].split(':').map!{|s| s.to_i}

    steam_id[1] + steam_id[2] * 2 + 76561197960265728
  end

  # Creates a new `SteamId` instance using a SteamID as used on servers
  #
  # The SteamID from the server is converted into a 64bit numeric SteamID first
  # before this is used to retrieve the corresponding Steam Community profile.
  #
  # @param [String] steam_id The SteamID string as used on servers, like
  #        `STEAM_0:0:12345`
  # @return [SteamId] The `SteamId` belonging to the given SteamID
  # @see .convert_steam_id_to_community_id
  # @see #initialize
  def self.from_steam_id(steam_id)
    new(convert_steam_id_to_community_id(steam_id))
  end

  # Creates a new `SteamId` instance for the given Steam ID
  #
  # @param [String, Fixnum] id The custom URL of the Steam ID specified by the
  #        user or the 64bit SteamID
  # @param [Boolean] fetch if `true` the Steam ID's data is loaded into the
  #        object
  def initialize(id, fetch = true)
    begin
      if id.is_a? Numeric
        @steam_id64 = id
      else
        @custom_url = id.downcase
      end

      super(fetch)
    rescue REXML::ParseException
      raise SteamCondenserException, 'SteamID could not be loaded.'
    end
  end

  # Returns the base URL for this Steam ID
  #
  # This URL is different for Steam IDs having a custom URL.
  #
  # @return [String] The base URL for this SteamID
  def base_url
    if @custom_url.nil?
      "http://steamcommunity.com/profiles/#{@steam_id64}"
    else
      "http://steamcommunity.com/id/#{@custom_url}"
    end
  end

  # Fetchs data from the Steam Community by querying the XML version of the
  # profile specified by the ID of this Steam ID
  #
  # @raise SteamCondenserException if the Steam ID data is not available, e.g.
  #        when it is private
  # @see Cacheable#fetch
  def fetch
    profile_url = open(base_url + '?xml=1', {:proxy => true})
    profile = REXML::Document.new(profile_url.read).root

    unless REXML::XPath.first(profile, 'error').nil?
      raise SteamCondenserException, profile.elements['error'].text
    end

    @nickname         = CGI.unescapeHTML profile.elements['steamID'].text
    @steam_id64       = profile.elements['steamID64'].text.to_i
    @vac_banned       = (profile.elements['vacBanned'].text == 1)

    unless REXML::XPath.first(profile, 'privacyMessage').nil?
      raise SteamCondenserException, profile.elements['privacyMessage'].text
    end

    @image_url        = profile.elements['avatarIcon'].text[0..-5]
    @online_state     = profile.elements['onlineState'].text
    @privacy_state    = profile.elements['privacyState'].text
    @state_message    = profile.elements['stateMessage'].text
    @visibility_state = profile.elements['visibilityState'].text.to_i

    if @privacy_state == 'public'
      @custom_url                       = profile.elements['customURL'].text.downcase
      @custom_url                       = nil if @custom_url.empty?

      # The favorite game cannot be set since 10/10/2008, but old profiles
      # still have this. May be removed in a future version.
      unless REXML::XPath.first(profile, 'favoriteGame').nil?
        @favorite_game                  = profile.elements['favoriteGame/name'].text
        @favorite_game_hours_played     = profile.elements['favoriteGame/hoursPlayed2wk'].text
      end

      @head_line    = CGI.unescapeHTML profile.elements['headline'].text
      @hours_played = profile.elements['hoursPlayed2Wk'].text.to_f
      @location     = profile.elements['location'].text
      @member_since = Time.parse(profile.elements['memberSince'].text)
      @real_name    = CGI.unescapeHTML profile.elements['realname'].text
      @steam_rating = profile.elements['steamRating'].text.to_f
      @summary      = CGI.unescapeHTML profile.elements['summary'].text

      @most_played_games = {}
      unless REXML::XPath.first(profile, 'mostPlayedGames').nil?
        profile.elements.each('mostPlayedGames/mostPlayedGame') do |most_played_game|
          @most_played_games[most_played_game.elements['gameName'].text] = most_played_game.elements['hoursPlayed'].text.to_f
        end
      end

      @groups = []
      unless REXML::XPath.first(profile, 'groups').nil?
        profile.elements.each('groups/group') do |group|
          @groups << SteamGroup.new(group.elements['groupID64'].text.to_i, false)
        end
      end

      @links = {}
      unless REXML::XPath.first(profile, 'mostPlayedGames').nil?
        profile.elements.each('weblinks/weblink') do |link|
          @links[CGI.unescapeHTML link.elements['title'].text] = link.elements['link'].text
        end
      end
    end

    super
  end

  # Fetches the friends of this user
  #
  # This creates a new `SteamId` instance for each of the friends without
  # fetching their data.
  #
  # @see #friends
  # @see #initialize
  def fetch_friends
    url = "#{base_url}/friends?xml=1"

    @friends = []
    friends_data = REXML::Document.new(open(url, {:proxy => true}).read).root
    friends_data.elements.each('friends/friend') do |friend|
      @friends << SteamId.new(friend.text.to_i, false)
    end
  end

  # Fetches the games this user owns
  #
  # This fills the game hash with the names of the games as keys. The values
  # will either be `false` if the game does not have stats or the game's
  # "friendly name".
  #
  # @see #games
  def fetch_games
    url = "#{base_url}/games?xml=1"

    @games = {}
    games_data = REXML::Document.new(open(url, {:proxy => true}).read).root
    games_data.elements.each('games/game') do |game|
      game_name = game.elements['name'].text
      if game.elements['globalStatsLink'].nil?
        @games[game_name] = false
      else
        friendly_name = game.elements['globalStatsLink'].text.match(/http:\/\/steamcommunity.com\/stats\/([^?\/]+)\/achievements\//)[1]
        @games[game_name] = friendly_name.downcase
      end
    end

    true
  end

  # Returns the URL of the full-sized version of this user's avatar
  #
  # @return [String] The URL of the full-sized avatar
  def full_avatar_url
    "#{@image_url}_full.jpg"
  end

  # Returns the stats for the given game for the owner of this SteamID
  #
  # @param [String] game_name The friendly name of the game stats should be
  #        fetched for
  # @return [GameStats] The statistics for the game with the given name
  # @raise [ArgumentError] if the user does not own this game or it does not
  #        have any stats
  def game_stats(game_name)
    if games.has_value? game_name
      friendly_name = game_name
    elsif games.has_key? game_name.downcase
      friendly_name = games[game_name.downcase]
    else
      raise ArgumentError, "Stats for game #{game_name} do not exist."
    end

    GameStats.create_game_stats(@custom_url || @steam_id64, friendly_name)
  end

  # Returns the Steam Community friends of this user
  #
  # If the friends haven't been fetched yet, this is done now.
  #
  # @return [Array<SteamId>] The friends of this user
  # @see #fetch_friends
  def friends
    fetch_friends if @friends.nil?
    @friends
  end

  # Returns the games this user owns
  #
  # The keys of the hash are the games' names and the values are the "friendly
  # names" used for stats or `false` if the games has no stats.
  #
  # If the friends haven't been fetched yet, this is done now.
  #
  # @return [Hash<String, Object>] Pairs of game names and friendly names
  # @see #fetch_games
  def games
    fetch_games if @games.nil?
    @games
  end

  # Returns the URL of the icon version of this user's avatar
  #
  # @return [String] The URL of the icon-sized avatar
  def icon_url
    "#{@image_url}.jpg"
  end

  # Returns whether the owner of this SteamID is VAC banned
  #
  # @return [Boolean] `true` if the user has been banned by VAC
  def is_banned?
    @vac_banned
  end

  # Returns whether the owner of this SteamId is playing a game
  #
  # @return [Boolean] `true` if the user is in-game
  def is_in_game?
    @online_state == 'in-game'
  end

  # Returns whether the owner of this SteamID is currently logged into Steam
  #
  # @return [Boolean] `true` if the user is online
  def is_online?
    @online_state != 'offline'
  end

  # Returns the URL of the medium-sized version of this user's avatar
  #
  # @return [String] The URL of the medium-sized avatar
  def medium_avatar_url
    "#{@image_url}_medium.jpg"
  end

end
