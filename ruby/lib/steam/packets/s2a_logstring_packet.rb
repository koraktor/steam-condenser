# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# The S2A_LOGSTRING packet type is used to transfer log messages.
class S2A_LOGSTRING_Packet

  include SteamPacket

  # The log message contained in this packet
  attr_reader :message

  # Creates a new log message packet
  def initialize(data)
    super S2A_LOGSTRING_HEADER, data

    @content_data.byte
    @message = @content_data.string
  end

end
