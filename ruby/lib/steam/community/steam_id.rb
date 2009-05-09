# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "open-uri"
require "rexml/document"

require "exceptions/steam_condenser_exception"
require "steam/community/game_stats"
require "steam/community/steam_group"

# The SteamId class represents a Steam Community profile (also called Steam ID)
class SteamId
  
  attr_reader :custom_url, :favorite_game, :favorite_game_hours_played,
              :friends, :groups, :head_line, :hours_played, :image_url, :links,
              :location, :member_since, :most_played_games, :privacy_state,
              :real_name, :state_message, :steam_rating, :steam_rating_text,
              :summary, :vac_banned, :visibility_state

  # Converts a SteamID as reported by game servers to a 64bit SteamID
  def self.convert_steam_id_to_community_id(steam_id)
    if steam_id == "STEAM_ID_LAN"
      raise SteamCondenserException.new("Cannot convert SteamID \"STEAM_ID_LAN\" to a community ID.")
    elsif steam_id.match(/STEAM_[0-1]:[0-1]:[0-9]+/).nil?
      raise SteamCondenserException.new("SteamID \"#{steam_id}\" doesn't have the correct format.")
    end
    
    steam_id = steam_id[6..-1].split(":").map!{|s| s.to_i}
    
    return steam_id[1] + 76561197960265728 + steam_id[2] * 2
  end

  # Creates a new SteamId object using a SteamID64 converted from a server
  # SteamID
  def self.get_from_steam_id(steam_id)
    return self.new(self.convert_steam_id_to_community_id(steam_id))
  end

  # Creates a new SteamId object for the given SteamID, either numeric or the
  # custom URL specified by the user. If fetch is true, fetch_data is used to
  # load data into the object. fetch defaults to true.
  def initialize(id, fetch = true)
    if id.is_a? Numeric
      @steam_id64 = id
    else
      @custom_url = id
    end

    begin
      self.fetch_data if fetch
    rescue REXML::ParseException
      raise SteamCondenserException.new("SteamID could not be loaded.")
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
  def fetch_data
    url = base_url << '?xml=1'

    profile_url = open(url, {:proxy => true})
    if profile_url.base_uri.to_s != url
      profile_url = open(profile_url.base_uri.to_s + "?xml=1", {:proxy => true})
    end
    
    profile = REXML::Document.new(profile_url.read).elements["profile"]
      
    @image_url        = profile.elements["avatarIcon"].text[0..-5]
    @online_state     = profile.elements["onlineState"].text
    @privacy_state    = profile.elements["privacyState"].text
    @state_message    = profile.elements["stateMessage"].text
    @steam_id         = profile.elements["steamID"].text
    @steam_id64       = profile.elements["steamID64"].text.to_i
    @vac_banned       = (profile.elements["vacBanned"].text == 1)
    @visibility_state = profile.elements["visibilityState"].text.to_i
    
    # Only public profiles can be scanned for further information
    if @privacy_state == "public"
      @custom_url                       = profile.elements["customURL"].text

      unless REXML::XPath.first(profile, "favoriteGame").nil?
        @favorite_game                    = profile.elements["favoriteGame/name"].text
        @favorite_game_hours_played       = profile.elements["favoriteGame/hoursPlayed2wk"].text
      end

      @head_line                        = profile.elements["headline"].text
      @hours_played                     = profile.elements["hoursPlayed2Wk"].text.to_f
      @location                         = profile.elements["location"].text
      @member_since                     = Time.parse profile.elements["memberSince"].text
      @real_name                        = profile.elements["realname"].text
      @steam_rating                     = profile.elements["steamRating"].text.to_f
      @summary                          = profile.elements["summary"].text

      unless REXML::XPath.first(profile, "mostPlayedGames").nil?
        @most_played_games = Hash.new
        profile.elements["mostPlayedGames"].elements.each("mostPlayedGame") do |most_played_game|
          @most_played_games[most_played_game.elements["gameName"].text] = most_played_game.elements["hoursPlayed"].text.to_f
        end
      end
      
      @friends = Array.new
      profile.elements["friends"].elements.each("friend") do |friend|
        @friends << SteamId.new(friend.elements["steamID64"].text.to_i, false)
      end
      
      @groups = Array.new
      profile.elements["groups"].elements.each("group") do |group|
        @groups << SteamGroup.new(group.elements["groupID64"].text.to_i)
      end
      
      @links = Hash.new
      profile.elements["weblinks"].elements.each("weblink") do |link|
        @links[link.elements["title"].text] = link.elements["link"].text
      end
    end
  end

  def fetch_games
    require 'rubygems'
    require 'Hpricot'

    games_data = Hpricot(open('/Users/koraktor/steamcommunity_koraktor_games.html').read).at('div#mainContents')
    games_data.traverse_some_element('h4') do |game|
      @games << game.inner_html
    end

    return true
  end
  
  # Returns the URL of the full version of this user's avatar
  def full_avatar_url
    return "#{@image_url}_full.jpg"
  end
  
  # Returns a GameStats object for the given game for the owner of this SteamID
  def get_game_stats(game_name)
    if @custom_url.nil?
      return GameStats.create_game_stats(@steam_id64, game_name)
    else
      return GameStats.create_game_stats(@custom_url, game_name)
    end
  end

  def get_games
    fetch_games if @games.nil?
    @games
  end
  
  # Returns the URL of the icon version of this user's avatar
  def icon_url
    return "#{@image_url}.jpg"
  end
  
  # Returns whether the owner of this SteamID is VAC banned
  def is_banned?
    return @vac_banned 
  end
  
  # Returns whether the owner of this SteamId is playing a game
  def is_in_game?
    return @online_state == "in-game"
  end
  
  # Returns whether the owner of this SteamID is currently logged into Steam
  def is_online?
    return @online_state != "offline"
  end
  
  # Returns the URL of the medium version of this user's avatar
  def medium_avatar_url
    return "#{@image_url}_medium.jpg"
  end

end
