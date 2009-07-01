# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require 'open-uri'
require 'rexml/document'

require 'exceptions/steam_condenser_exception'
require 'steam/community/game_stats'
require 'steam/community/steam_group'

# The SteamId class represents a Steam Community profile (also called Steam ID)
class SteamId
  
  attr_reader :custom_url, :favorite_game, :favorite_game_hours_played,
              :friends, :groups, :head_line, :hours_played, :image_url, :links,
              :location, :member_since, :most_played_games, :privacy_state,
              :real_name, :state_message, :steam_rating, :steam_rating_text,
              :summary, :vac_banned, :visibility_state

  @@steam_ids = {}

  # Returns whether the requested SteamID is already cached
  def self.cached?(id)
    unless id.is_a? Numeric
      id.downcase!
    end
    @@steam_ids.key?(id)
  end

  # Clears the SteamID cache
  def self.clear_cache
    @@steam_ids = {}
  end

  # Converts a SteamID as reported by game servers to a 64bit SteamID
  def self.convert_steam_id_to_community_id(steam_id)
    if steam_id == 'STEAM_ID_LAN'
      raise SteamCondenserException.new('Cannot convert SteamID "STEAM_ID_LAN" to a community ID.')
    elsif steam_id.match(/STEAM_[0-1]:[0-1]:[0-9]+/).nil?
      raise SteamCondenserException.new("SteamID \"#{steam_id}\" doesn't have the correct format.")
    end
    
    steam_id = steam_id[6..-1].split(':').map!{|s| s.to_i}
    
    steam_id[1] + 76561197960265728 + steam_id[2] * 2
  end

  # Creates a new SteamId object using a SteamID64 converted from a server
  # SteamID
  def self.get_from_steam_id(steam_id)
    self.new(self.convert_steam_id_to_community_id(steam_id))
  end

  # This checks the cache for an existing SteamID. If it exists it is returned.
  # Otherwise a new SteamID is created.
  # Overrides the default constructor.
  def self.new(id, fetch = true)
    if cached?(id)
      @@steam_ids[id]
    else
      super(id, fetch)
    end
  end

  # Creates a new SteamId object for the given SteamID, either numeric or the
  # custom URL specified by the user. If +fetch+ is +true+ (default), fetch_data
  # is used to load data into the object.
  def initialize(id, fetch = true)
    if id.is_a? Numeric
      @steam_id64 = id
    else
      @custom_url = id.downcase
    end

    begin
      self.fetch_data if fetch
    rescue REXML::ParseException
      raise SteamCondenserException.new('SteamID could not be loaded.')
    end

    cache
  end

  # Returns the base URL for this SteamID
  def base_url
    if @custom_url.nil?
      "http://steamcommunity.com/profiles/#{@steam_id64}"
    else
      "http://steamcommunity.com/id/#{@custom_url}"
    end
  end

  # Saves this SteamID in the cache
  def cache
    unless @@steam_ids.key?(@steam_id64)
      @@steam_ids[@steam_id64] = self
      unless @custom_url.nil? or @@steam_ids.key?(@custom_url)
        @@steam_ids[@custom_url] = self
      end
    end

    true
  end
  
  # Fetchs data from the Steam Community by querying the XML version of the
  # profile specified by the ID of this SteamID
  def fetch_data
    profile_url = open(base_url + '?xml=1', {:proxy => true})
    profile = REXML::Document.new(profile_url.read).root
      
    @image_url        = profile.elements['avatarIcon'].text[0..-5]
    @online_state     = profile.elements['onlineState'].text
    @privacy_state    = profile.elements['privacyState'].text
    @state_message    = profile.elements['stateMessage'].text
    @steam_id         = profile.elements['steamID'].text
    @steam_id64       = profile.elements['steamID64'].text.to_i
    @vac_banned       = (profile.elements['vacBanned'].text == 1)
    @visibility_state = profile.elements['visibilityState'].text.to_i
    
    # Only public profiles can be scanned for further information
    if @privacy_state == 'public'
      @custom_url                       = profile.elements['customURL'].text.downcase

      unless REXML::XPath.first(profile, 'favoriteGame').nil?
        @favorite_game                    = profile.elements['favoriteGame/name'].text
        @favorite_game_hours_played       = profile.elements['favoriteGame/hoursPlayed2wk'].text
      end

      @head_line                        = profile.elements['headline'].text
      @hours_played                     = profile.elements['hoursPlayed2Wk'].text.to_f
      @location                         = profile.elements['location'].text
      @member_since                     = Time.parse profile.elements['memberSince'].text
      @real_name                        = profile.elements['realname'].text
      @steam_rating                     = profile.elements['steamRating'].text.to_f
      @summary                          = profile.elements['summary'].text

      unless REXML::XPath.first(profile, 'mostPlayedGames').nil?
        @most_played_games = Hash.new
        profile.elements['mostPlayedGames'].elements.each('mostPlayedGame') do |most_played_game|
          @most_played_games[most_played_game.elements['gameName'].text] = most_played_game.elements['hoursPlayed'].text.to_f
        end
      end
      
      @friends = Array.new
      profile.elements['friends'].elements.each('friend') do |friend|
        @friends << SteamId.new(friend.elements['steamID64'].text.to_i, false)
      end
      
      @groups = Array.new
      profile.elements['groups'].elements.each('group') do |group|
        @groups << SteamGroup.new(group.elements['groupID64'].text.to_i, false)
      end
      
      @links = Hash.new
      profile.elements['weblinks'].elements.each('weblink') do |link|
        @links[link.elements['title'].text] = link.elements['link'].text
      end
    end

    true
  end

  # Fetches the games this user owns
  def fetch_games
    require 'rubygems'
    require 'Hpricot'

    url = base_url << '/games'

    @games = {}
    games_data = Hpricot(open(url).read).at('div#mainContents')

    games_data.traverse_some_element('h4') do |game|
      game_name = game.inner_html
      stats = game.next_sibling
      if stats.name == 'br'
        @games[game_name] = false
      else
        stats = stats.next_sibling if stats.name == 'h5'
        if stats.name == 'br'
          @games[game_name] = false
        else
          stats = stats.siblings_at(2)[0]
          friendly_name = stats['href'].match(/http:\/\/steamcommunity.com\/stats\/([0-9a-zA-Z:]+)\/achievements\//)[1]
          @games[game_name] = friendly_name.downcase
        end
      end
    end

    true
  end
  
  # Returns the URL of the full version of this user's avatar
  def full_avatar_url
    "#{@image_url}_full.jpg"
  end
  
  # Returns a GameStats object for the given game for the owner of this SteamID
  def game_stats(game_name)
    game_name.downcase!
    
    if games.has_value? game_name
      friendly_name = game_name
    elsif games.has_key? game_name
      friendly_name = games[game_name]
    else
      raise ArgumentError.new("Stats for game #{game_name} do not exist.")
    end

    GameStats.create_game_stats(@custom_url || @steam_id64, friendly_name)
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
  
  # Returns the URL of the medium version of this user's avatar
  def medium_avatar_url
    "#{@image_url}_medium.jpg"
  end

end
