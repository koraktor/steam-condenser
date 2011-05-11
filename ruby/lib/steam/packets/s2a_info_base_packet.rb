# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This module implements methods to generate and access server information from
# S2A_INFO_DETAILED and S2A_INFO2 response packets
#
# @author Sebastian Staudt
# @see S2A_INFO_DETAILED_Packet
# @see S2A_INFO2_Packet
module S2A_INFO_BasePacket

  include SteamPacket

  # Returns the information provided by the server
  #
  # @return [Hash<String, Object>] The information provided by the server
  attr_reader :info_hash

  protected

  # Generates a hash of server properties from the instance variables of the
  # including packet object
  def generate_info_hash
    @info_hash = Hash[
      *instance_variables.map { |var|
        [var[1..-1], instance_variable_get(var)] if var != '@content_data' && var != '@header_data'
      }.compact.flatten
    ]
  end

end
