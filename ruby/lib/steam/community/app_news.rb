# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'multi_json'

require 'steam/community/web_api'

# This class represents Steam news and can be used to load a list of current
# news about specific games
#
# @author Sebastian Staudt
class AppNews

  # Returns the Steam Application ID of the game this news belongs to
  #
  # @return [Fixnum] The application ID of the game this news belongs to
  attr_reader :app_id

  # Returns the name of the author of this news
  #
  # @return [String] The author of this news
  attr_reader :author

  # Returns the contents of this news
  #
  # This might contain HTML code.
  #
  # @note Depending on the setting for the maximum length of a news (see
  #       {.news_for_app}), the contents might be truncated.
  # @return [String] The contents of this news
  attr_reader :contents

  # Returns the date this news item has been published
  #
  # @return [Time] The date this news has been published
  attr_reader :date

  # Returns the name of the feed this news item belongs to
  #
  # @return [String] The name of the feed this news belongs to
  attr_reader :feed_label

  # Returns the symbolic name of the feed this news item belongs to
  #
  # @return [String] The symbolic name of the feed this news belongs to
  attr_reader :feed_name

  # Returns a unique identifier for this news
  #
  # @return [Fixnum] A unique identifier for this news
  attr_reader :gid

  # Returns the title of this news
  #
  # @return [String] The title of this news
  attr_reader :title

  # Returns the URL of the original news
  #
  # This is a direct link to the news on the Steam website or a redirecting
  # link to the external post.
  #
  # @return [String] The URL of the original news
  attr_reader :url

  # Loads the news for the given game with the given restrictions
  #
  # @param [Fixnum] app_id The unique Steam Application ID of the game (e.g.
  #        `440` for Team Fortress 2). See
  #        http://developer.valvesoftware.com/wiki/Steam_Application_IDs for
  #        all application IDs.
  # @param [Fixnum] count The maximum number of news to load. There's no
  #        reliable way to load all news. Use a really great number instead.
  # @param [Fixnum] max_length The maximum content length of the news. If a
  #        maximum length is defined, the content of the news will only be at
  #        most `max_length` characters long plus an ellipsis.
  # @return [Array<AppNews>] An array of news items for the specified game with
  #         the given options
  def self.news_for_app(app_id, count = 5, max_length = nil)
    params = { :appid => app_id, :count => count, :maxlength => max_length }
    data = WebApi.json('ISteamNews', 'GetNewsForApp', 2, params)

    news_items = []
    MultiJson.decode(data, { :symbolize_keys => true })[:appnews][:newsitems].each do |news_data|
      news_items << AppNews.new(app_id, news_data)
    end

    news_items
  end

  # Returns whether this news item originates from a source other than Steam
  # itself (e.g. an external blog)
  #
  # @return [Boolean] `true` if this news item is from an external source
  def external?
    @external
  end

  # Returns a simple textual representation of this news item
  #
  # Will consist of the name of the feed this news belongs to and the title of
  # the news.
  #
  # @return [String] A simple text representing this news
  def to_s
    "#{@feed_label}: #{@title}"
  end

  private

  # Creates a new instance of an AppNews news item with the given data
  #
  # @param [Fixnum] app_id The unique Steam Application ID of the game (e.g.
  #        `440` for Team Fortress 2). See
  #        http://developer.valvesoftware.com/wiki/Steam_Application_IDs for
  #        all application IDs.
  # @param [Hash<Symbol, Object>] news_data The news data extracted from JSON
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
