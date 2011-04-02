# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'open-uri'
require 'rexml/document'

require 'exceptions/steam_condenser_exception'
require 'steam/community/cacheable'
require 'steam/community/game_stats'
require 'steam/community/steam_group'

# The SteamId class represents a Steam Community profile (also called Steam ID)
class SteamId

  include Cacheable
  cacheable_with_ids :custom_url, :steam_id64

  attr_reader :custom_url, :favorite_game, :favorite_game_hours_played,
              :groups, :head_line, :hours_played, :image_url, :links, :location,
              :member_since, :most_played_games, :nickname, :privacy_state,
              :real_name, :state_message, :steam_id64, :steam_rating,
              :steam_rating_text, :summary, :vac_banned, :visibility_state

  # Converts the 64bit SteamID +community_id+ as used and reported by the Steam
  # Community to a SteamID reported by game servers
  def self.convert_community_id_to_steam_id(community_id)
    steam_id1 = community_id % 2
    steam_id2 = community_id - 76561197960265728

    unless steam_id2 > 0
      raise SteamCondenserException.new("SteamID #{community_id} is too small.")
    end

    steam_id2 = (steam_id2 - steam_id1) / 2

    "STEAM_0:#{steam_id1}:#{steam_id2}"
  end

  # Converts the SteamID +steam_id+ as reported by game servers to a 64bit
  # SteamID
  def self.convert_steam_id_to_community_id(steam_id)
    if steam_id == 'STEAM_ID_LAN' or steam_id == 'BOT'
      raise SteamCondenserException.new("Cannot convert SteamID \"#{steam_id}\" to a community ID.")
    elsif steam_id.match(/^STEAM_[0-1]:[0-1]:[0-9]+$/).nil?
      raise SteamCondenserException.new("SteamID \"#{steam_id}\" doesn't have the correct format.")
    end

    steam_id = steam_id[6..-1].split(':').map!{|s| s.to_i}

    steam_id[1] + steam_id[2] * 2 + 76561197960265728
  end

  # Creates a new SteamId object using the SteamID64 converted from a server
  # SteamID given by +steam_id+
  def self.from_steam_id(steam_id)
    new(convert_steam_id_to_community_id(steam_id))
  end

  # Creates a new SteamId object for the given SteamID +id+, either numeric or
  # the custom URL specified by the user. If +fetch+ is +true+ (default),
  # fetch_data is used to load data into the object.
  def initialize(id, fetch = true)
    begin
      if id.is_a? Numeric
        @steam_id64 = id
      else
        @custom_url = id.downcase
      end

      super(fetch)
    rescue REXML::ParseException
      raise SteamCondenserException.new('SteamID could not be loaded.')
    end
  end

  # Returns the base URL for this SteamID
  def base_url
    if @custom_url.nil?
      "http://steamcommunity.com/profiles/#{@steam_id64}"
    else
      "http://steamcommunity.com/id/#{@custom_url}"
    end
  end

  # Fetchs data from the Steam Community by querying the XML version of the
  # profile specified by the ID of this SteamID
  def fetch
    profile_url = open(base_url + '?xml=1', {:proxy => true})
    profile = REXML::Document.new(profile_url.read).root

    unless REXML::XPath.first(profile, 'error').nil?
      raise SteamCondenserException.new(profile.elements['error'].text)
    end

    @nickname         = profile.elements['steamID'].text
    @steam_id64       = profile.elements['steamID64'].text.to_i
    @vac_banned       = (profile.elements['vacBanned'].text == 1)

    unless REXML::XPath.first(profile, 'privacyMessage').nil?
      raise SteamCondenserException.new(profile.elements['privacyMessage'].text)
    end

    @image_url        = profile.elements['avatarIcon'].text[0..-5]
    @online_state     = profile.elements['onlineState'].text
    @privacy_state    = profile.elements['privacyState'].text
    @state_message    = profile.elements['stateMessage'].text
    @visibility_state = profile.elements['visibilityState'].text.to_i

    # Only public profiles can be scanned for further information
    if @privacy_state == 'public'
      @custom_url                       = profile.elements['customURL'].text.downcase
      @custom_url                       = nil if @custom_url.empty?

      # The favorite game cannot be set since 10/10/2008, but old profiles
      # still have this. May be removed in a future version.
      unless REXML::XPath.first(profile, 'favoriteGame').nil?
        @favorite_game                  = profile.elements['favoriteGame/name'].text
        @favorite_game_hours_played     = profile.elements['favoriteGame/hoursPlayed2wk'].text
      end

      @head_line                        = profile.elements['headline'].text
      @hours_played                     = profile.elements['hoursPlayed2Wk'].text.to_f
      @location                         = profile.elements['location'].text
      @member_since                     = Time.parse(profile.elements['memberSince'].text)
      @real_name                        = profile.elements['realname'].text
      @steam_rating                     = profile.elements['steamRating'].text.to_f
      @summary                          = profile.elements['summary'].text

      # The most played games only exist if a user played at least one game in
      # the last two weeks
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
          @links[link.elements['title'].text] = link.elements['link'].text
        end
      end
    end

    super
  end

  # Fetches the friends of this user
  def fetch_friends
    url = "#{base_url}/friends?xml=1"

    @friends = []
    friends_data = REXML::Document.new(open(url, {:proxy => true}).read).root
    friends_data.elements.each('friends/friend') do |friend|
      @friends << SteamId.new(friend.text.to_i, false)
    end
  end

  # Fetches the games this user owns
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
  def full_avatar_url
    "#{@image_url}_full.jpg"
  end

  # Returns a GameStats object for the given game for the owner of this SteamID
  def game_stats(game_name)
    if games.has_value? game_name
      friendly_name = game_name
    elsif games.has_key? game_name.downcase
      friendly_name = games[game_name.downcase]
    else
      raise ArgumentError.new("Stats for game #{game_name} do not exist.")
    end

    GameStats.create_game_stats(@custom_url || @steam_id64, friendly_name)
  end

  # Returns an Array of SteamId representing all Steam Community friends of this
  # user.
  def friends
    fetch_friends if @friends.nil?
    @friends
  end

  # Returns a Hash with the games this user owns. The keys are the games' names
  # and the values are the "friendly names" used for stats or +false+ if the
  # games has no stats.
  def games
    fetch_games if @games.nil?
    @games
  end

  # Returns the URL of the icon version of this user's avatar
  def icon_url
    "#{@image_url}.jpg"
  end

  # Returns whether the owner of this SteamID is VAC banned
  def is_banned?
    @vac_banned
  end

  # Returns whether the owner of this SteamId is playing a game
  def is_in_game?
    @online_state == 'in-game'
  end

  # Returns whether the owner of this SteamID is currently logged into Steam
  def is_online?
    @online_state != 'offline'
  end

  # Returns the URL of the medium-sized version of this user's avatar
  def medium_avatar_url
    "#{@image_url}_medium.jpg"
  end

end
