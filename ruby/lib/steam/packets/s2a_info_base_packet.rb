# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/steam_packet'

# The S2A_INFO_BasePacket class represents the response to a A2S_INFO
# request send to the server.
module S2A_INFO_BasePacket

  include SteamPacket

  attr_reader :info_hash

  # Returns the hash containing information on the server
  def generate_info_hash
    @info_hash = Hash[
      *instance_variables.map { |var|
        [var[1..-1], instance_variable_get(var)] if var != '@content_data' && var != '@header_data'
      }.compact.flatten
    ]
  end

end
