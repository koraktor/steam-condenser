# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'json'

require 'steam/community/web_api'

# The AppNews class is a representation of Steam news and can be used to load
# current news about specific games
class AppNews

  attr_reader :app_id, :author, :contents, :date, :feed_label, :feed_name,
              :gid, :title, :url

  # Loads the news for the given game with the given restrictions
  #
  # [+app_id+]     The unique Steam Application ID of the game (e.g. +440+ for
  #                Team Fortress 2). See
  #                http://developer.valvesoftware.com/wiki/Steam_Application_IDs
  #                for all application IDs
  # [+count+]      The maximum number of news to load (default: 5). There's no
  #                reliable way to load all news. Use really a really great
  #                number instead
  # [+max_length+] The maximum content length of the news (default: nil). If a
  #                maximum length is defined, the content of the news will only
  #                be at most +max_length+ characters long plus an ellipsis
  def self.news_for_app(app_id, count = 5, max_length = nil)
    params = { :appid => app_id, :count => count, :maxlength => max_length }
    data = WebApi.json('ISteamNews', 'GetNewsForApp', 2, params)

    news_items = []
    JSON.parse(data, { :symbolize_names => true })[:appnews][:newsitems].each do |news_data|
      news_items << AppNews.new(app_id, news_data)
    end

    news_items
  end

  # Returns whether this news items originates from a source other than Steam
  # itself (e.g. an external blog)
  def external?
    @external
  end

  private

  # Creates a new instance of an AppNews news item with the given data
  #
  # [+app_id+]    The unique Steam Application ID of the game (e.g. +440+ for
  #               Team Fortress 2). See
  #               http://developer.valvesoftware.com/wiki/Steam_Application_IDs
  #               for all application IDs
  # [+news_data+] The news data extracted from JSON
  def initialize(app_id, news_data)
    @app_id      = app_id
    @author      = news_data[:author]
    @contents    = news_data[:contents].strip
    @data        = Time.at(news_data[:date])
    @external    = news_data[:is_external_url]
    @feed_label  = news_data[:feedlabel]
    @feed_name   = news_data[:feedname]
    @gid         = news_data[:gid]
    @title       = news_data[:title]
    @url         = news_data[:url]
  end

end
