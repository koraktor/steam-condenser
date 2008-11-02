# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "open-uri"
require "rexml/document"

require "steam/community/game_stats"
require "steam/community/steam_group"
require "steam/community/tf2/tf2_stats"


# The SteamId class represents a Steam Community profile (also called Steam ID)
class SteamId
  
  attr_reader :custom_url, :favorite_game, :favorite_game_hours_played,
              :friends, :groups, :head_line, :hours_played, :image_url, :links,
              :location, :member_since, :most_played_games, :privacy_state,
              :real_name, :state_message, :steam_rating, :steam_rating_text,
              :summary, :vac_banned, :visibility_state

  # Creates a new SteamId object for the given SteamID, either numeric or the
  # custom URL specified by the user. If fetch is true, fetch_data is used to
  # load data into the object. fetch defaults to true.
  def initialize(id, fetch = true)
    @id = id
    
    begin
      self.fetch_data if fetch
    rescue REXML::ParseException
      raise Exception.new("SteamID could not be loaded.")
    end
  end
  
  # Fetchs data from the Steam Community by querying the XML version of the
  # profile specified by the ID of this SteamID
  def fetch_data
    profile = REXML::Document.new(open("http://www.steamcommunity.com/id/#{@id}?xml=1", {:proxy => true}).read).elements["profile"]
      
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
      @favorite_game                    = profile.elements["favoriteGame"].elements["name"].text
      @favorite_game_hours_played       = profile.elements["favoriteGame"].elements["hoursPlayed2wk"].text
      @head_line                        = profile.elements["headline"].text
      @hours_played                     = profile.elements["hoursPlayed2Wk"].text.to_f
      @location                         = profile.elements["location"].text
      @member_since                     = Time.parse profile.elements["memberSince"].text
      @real_name                        = profile.elements["realname"].text
      @steam_rating, @steam_rating_text = profile.elements["steamRating"].text.split(" - ")
      @summary                          = profile.elements["summary"].text
      
      @most_played_games = Hash.new
      profile.elements["mostPlayedGames"].elements.each("mostPlayedGame") do |most_played_game|
        @most_played_games[most_played_game.elements["gameName"].text] = most_played_game.elements["hoursPlayed"].text.to_f
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
  
  # Returns the URL of the full version of this user's avatar
  def full_avatar_url
    return "#{@image_url}_full.jpg"
  end
  
  # Returns a GameStats object for the given game for the owner of this SteamID
  def get_game_stats(game_name)
    if game_name == "TF2"
      return TF2Stats.new(@custom_url)
    else
      return GameStats.new(@custom_url, game_name)
    end
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
