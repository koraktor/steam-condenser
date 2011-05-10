# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/steam_packet'
require 'steam/servers/master_server'

# This packet class represents a A2M_GET_SERVERS_BATCH2 request sent to a
# master server
#
# It is used to receive a list of game servers matching the specified filters.
#
# Filtering:
# Instead of filtering the results sent by the master server locally, you
# should at least use the following filters to narrow down the results sent by
# the master server. Receiving all servers from the master server is taking
# quite some time.
#
# Available filters:
#
# * `\type\d`: Request only dedicated servers
# * `\secure\1`: Request only secure servers
# * `\gamedir\[mod]`: Request only servers of a specific mod
# * `\map\[mapname]`: Request only servers running a specific map
# * `\linux\1`: Request only linux servers
# * `\emtpy\1`: Request only **non**-empty servers
# * `\full\`: Request only servers **not** full
# * `\proxy\1`: Request only spectator proxy servers
#
# @author Sebastian Staudt
# @see MasterServer#servers
class A2M_GET_SERVERS_BATCH2_Packet

  include SteamPacket

  # Creates a master server request, filtering by the given paramters
  #
  # @param [Numeric] region_code The region code to filter servers by region
  # @param [String] start_ip This should be the last IP received from the
  #        master server or 0.0.0.0
  # @param [String] filter The filters to apply in the form
  #        "\filtername\value..."
  def initialize(region_code = MasterServer::REGION_ALL, start_ip = '0.0.0.0:0', filter = '')
    super A2M_GET_SERVERS_BATCH2_HEADER

    @filter = filter
    @region_code = region_code
    @start_ip = start_ip
  end

  # Returns the raw data representing this packet
  #
  # @return [String] A string containing the raw data of this request packet
  def to_s
    [@header_data, @region_code, @start_ip, @filter].pack('c2Z*Z*')
  end

end
