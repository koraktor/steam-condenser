# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "open-uri"
require "rexml/document"

require "steam/community/steam_group"

# The SteamId class represents a Steam Community profile (also called Steam ID)
class SteamId
  
  attr_reader :custom_url, :favorite_game, :favorite_game_hours_played,
              :friends, :groups, :head_line, :hours_played, :image_url, :links,
              :location, :member_since, :most_played_games, :online_state,
              :privacy_state, :real_name, :state_message, :steam_rating,
              :steam_rating_text, :summary, :vac_banned, :visibility_state

  # Creates a new SteamId object by querying the XML version of the profile
  # corresponding to the given ID
  def initialize(id, fetch_data = true)
    @id = id
    
    if fetch_data
      profile = REXML::Document.new(open("http://www.steamcommunity.com/id/#{id}?xml=1", {:proxy => true}).read).elements["profile"]
      
      @image_url        = profile.elements["avatarIcon"].text[0..-5]
      @online_state     = (profile.elements["onlineState"].text == "online")
      @privacy_state    = profile.elements["privacyState"].text
      @state_message    = profile.elements["stateMessage"].text
      @steam_id         = profile.elements["steamID"].text
      @steam_id64       = profile.elements["steamID64"].text
      @vac_banned       = (profile.elements["vacBanned"].text == 1)
      @visibility_state = profile.elements["visibilityState"].text
      
      # Only public profiles can be scanned for further information
      if @privacy_state == "public"
        @custom_url                       = profile.elements["customURL"].text
        @favorite_game                    = profile.elements["favoriteGame"].elements["name"].text
        @favorite_game_hours_played       = profile.elements["favoriteGame"].elements["hoursPlayed2wk"].text
        @head_line                        = profile.elements["headline"].text
        @hours_played                     = profile.elements["hoursPlayed2Wk"].text
        @location                         = profile.elements["location"].text
        @member_since                     = Time.parse profile.elements["memberSince"].text
        @real_name                        = profile.elements["realname"].text
        @steam_rating, @steam_rating_text = profile.elements["steamRating"].text.split(" - ")
        @summary                          = profile.elements["summary"].text
        
        @most_played_games = Hash.new
        profile.elements["mostPlayedGames"].elements.each("mostPlayedGame") do |most_played_game|
          @most_played_games[most_played_game.elements["gameName"].text] = most_played_game.elements["hoursPlayed"].text
        end
        
        @friends = Array.new
        profile.elements["friends"].elements.each("friend") do |friend|
          @friends << SteamId.new(friend.elements["steamID64"].text, false)
        end
        
        @groups = Array.new
        profile.elements["groups"].elements.each("group") do |group|
          @groups << SteamGroup.new(group.elements["groupID64"].text)
        end
        
        @links = Hash.new
        profile.elements["weblinks"].elements.each("weblink") do |link|
          @links[link.elements["title"].text] = link.elements["link"].text
        end
      end
    end
  end
  
  # Returns the URL of the full version of this user's avatar
  def full_avatar_url
    return "#{@image_url}_full.jpg"
  end
  
  # Returns the URL of the icon version of this user's avatar
  def icon_url
    return "#{@image_url}.jpg"
  end
  
  # Returns the URL of the medium version of this user's avatar
  def medium_avatar_url
    return "#{@image_url}_medium.jpg"
  end

end
