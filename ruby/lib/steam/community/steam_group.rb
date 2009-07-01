# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require 'open-uri'
require 'rexml/document'

require 'steam/community/steam_id'

# The SteamGroup class represents a group in the Steam Community
class SteamGroup

  attr_reader :custom_url, :group_id64
  
  @@steam_groups = {}

  # Returns whether the requested group is already cached
  def self.cached?(id)
    unless id.is_a? Numeric
      id.downcase!
    end
    @@steam_groups.key?(id)
  end

  # Clears the group cache
  def self.clear_cache
    @@steam_groups = {}
  end

  # This checks the cache for an existing group. If it exists it is returned.
  # Otherwise a new SteamGroup object is created.
  # Overrides the default constructor.
  def self.new(id, fetch = true)
    if cached?(id)
      @@steam_groups[id]
    else
      super(id, fetch)
    end
  end

  # Creates a SteamGroup object with the given group ID
  def initialize(id, fetch = false)
    if id.is_a? Numeric
      @group_id64 = id
    else
      @custom_url = id
    end

    begin
      fetch_members if fetch
    rescue REXML::ParseException
      raise SteamCondenserException.new('SteamID could not be loaded.')
    end

    cache
  end

  # Saves this group in the cache
  def cache
    unless @@steam_groups.key?(@group_id64)
      @@steam_groups[@group_id64] = self
      unless @custom_url.nil? or @@steam_groups.key?(@custom_url)
        @@steam_groups[@custom_url] = self
      end
    end

    true
  end

  # Returns the URL to the group's Steam Community page
  def base_url
    if @custom_url.nil?
      "http://steamcommunity.com/gid/#{@group_id64}"
    else
      "http://steamcommunity.com/groups/#{@custom_url}"
    end
  end

  # Parses the data about this groups members
  def fetch_members
    @members = []
    page = 0

    begin
      page += 1
      url = open("#{base_url}/memberslistxml?p=#{page}", {:proxy => true})
      @member_data = REXML::Document.new(url.read).root

      if page == 1
        @group_id64 = @member_data.elements['groupID64'].text.to_i
      end
      total_pages = @member_data.elements['totalPages'].text.to_i
    
      @member_data.elements['members'].elements.each do |member|
        @members << SteamId.new(member.text.to_i, false)
      end
    end while page < total_pages

    true
  end

  # Returns the number of members this group has.
  # If the members have already been fetched with +fetch_members+ the size of
  # the member array is returned. Otherwise the group size is separately
  # fetched.
  def member_count
    if @members.nil?
      url = open("#{base_url}/memberslistxml", {:proxy => true})
      REXML::Document.new(url.read).root.elements['memberCount'].text.to_i
    else
      @members.size
    end
  end

  # Returns the members of this group
  # Calls +fetch_members+ if the members haven't been fetched already.
  def members
    fetch_members if @members.nil? or @members[0].nil?
    @members
  end
  
end