# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/steam_packet'

module RCONPacket

  include SteamPacket

  SERVERDATA_AUTH = 3
  SERVERDATA_AUTH_RESPONSE = 2
  SERVERDATA_EXECCOMMAND = 2
  SERVERDATA_RESPONSE_VALUE = 0

  attr_reader :request_id

  def initialize(request_id, rcon_header, rcon_data)
    super rcon_header, "#{rcon_data}\0\0"

    @request_id = request_id
  end

  def to_s
    [@content_data.length + 8, @request_id, @header_data, @content_data.string].pack('V3a*')
  end

end
