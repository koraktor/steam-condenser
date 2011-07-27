# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'open-uri'
require 'rexml/document'

require 'steam/community/cacheable'
require 'steam/community/steam_id'

# The SteamGroup class represents a group in the Steam Community
#
# @author Sebastian Staudt
class SteamGroup

  include Cacheable
  cacheable_with_ids :custom_url, :group_id64

  # Returns the custom URL of this group
  #
  # The custom URL is a admin specified unique string that can be used instead
  # of the 64bit SteamID as an identifier for a group.
  #
  # @return [String] The custom URL of this group
  attr_reader :custom_url

  # Returns this group's 64bit SteamID
  #
  # @return [Fixnum] This group's 64bit SteamID
  attr_reader :group_id64

  # Creates a new `SteamGroup` instance for the group with the given ID
  #
  # @param [String, Fixnum] id The custom URL of the group specified by the
  #        group admin or the 64bit group ID
  # @param [Boolean] fetch if `true` the groups's data is loaded into the
  #        object
  def initialize(id, fetch = true)
    begin
      if id.is_a? Numeric
        @group_id64 = id
      else
        @custom_url = id.downcase
      end

      super(fetch)
    rescue REXML::ParseException
      raise SteamCondenserException, 'Group could not be loaded.'
    end
  end

  # Returns the base URL for this group's page
  #
  # This URL is different for groups having a custom URL.
  #
  # @return [String] The base URL for this group
  def base_url
    if @custom_url.nil?
      "http://steamcommunity.com/gid/#{@group_id64}"
    else
      "http://steamcommunity.com/groups/#{@custom_url}"
    end
  end

  # Loads the members of this group
  #
  # This might take several HTTP requests as the Steam Community splits this
  # data over several XML documents if the group has lots of members.
  #
  # @see Cacheable#fetch
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

  # Returns the number of members this group has
  #
  # If the members have already been fetched the size of the member array is
  # returned. Otherwise the group size is separately fetched without needing
  # multiple requests for big groups.
  #
  # @return [Fixnum] The number of this group's members
  def member_count
    if @members.nil?
      url = open("#{base_url}/memberslistxml", {:proxy => true})
      REXML::Document.new(url.read).root.elements['memberCount'].text.to_i
    else
      @members.size
    end
  end

  # Returns the members of this group
  #
  # If the members haven't been fetched yet, this is done now.
  #
  # @return [Array<SteamId>] The Steam ID's of the members of this group
  # @see #fetch
  def members
    fetch if @members.nil? || @members[0].nil?
    @members
  end

end
