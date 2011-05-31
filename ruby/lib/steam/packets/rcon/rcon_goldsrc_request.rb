# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This packet class represents a RCON request packet sent to a GoldSrc server
#
# It is used to request a command execution on the server.
#
# @author Sebastian Staudt
# @see GoldSrcServer#rcon_exec
class RCONGoldSrcRequest

  include SteamPacket

  # Creates a request for the given request string
  #
  # The request string has the form `rcon {challenge number} {RCON password}
  # {command}`.
  #
  # @param [String] request The request string to send to the server
  def initialize(request)
    super 0x00, request
  end

  # Returns the raw data representing this packet
  #
  # @return [String] A string containing the raw data of this request packet
  def to_s
    [0xFFFFFFFF, @content_data.string].pack('Va*')
  end

end
