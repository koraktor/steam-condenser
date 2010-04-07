# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'open-uri'
require 'rexml/document'

require 'steam/community/cacheable'
require 'steam/community/steam_id'

# The SteamGroup class represents a group in the Steam Community
class SteamGroup

  include Cacheable
  cacheable_with_ids :custom_url, :group_id64

  attr_reader :custom_url, :group_id64

  # Creates a SteamGroup object with the given group ID
  def initialize(id, fetch = true)
    begin
      if id.is_a? Numeric
        @group_id64 = id
      else
        @custom_url = id.downcase
      end

      super(fetch)
    rescue REXML::ParseException
      raise SteamCondenserException.new('Group could not be loaded.')
    end
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
  def fetch
    @members = []
    page = 0

    begin
      page += 1
      url = open("#{base_url}/memberslistxml?p=#{page}", {:proxy => true})
      member_data = REXML::Document.new(url.read).root

      @group_id64 = member_data.elements['groupID64'].text.to_i if page == 1
      total_pages = member_data.elements['totalPages'].text.to_i

      member_data.elements['members'].elements.each do |member|
        @members << SteamId.new(member.text.to_i, false)
      end
    end while page < total_pages

    super
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
  # Calls +fetch+ if the members haven't been fetched already.
  def members
    fetch if @members.nil? || @members[0].nil?
    @members
  end

end
