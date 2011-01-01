# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# The M2S_REQUESTRESTART packet type is used to by master servers to request a
# game server restart, e.g. when using outdated versions.
class M2S_REQUESTRESTART_Packet

  include SteamPacket

  # Returns the challenge number used for master server communication
  attr_reader :challenge

  # Creates a new server restart request packet sent by a master server
  def initialize(data)
    super C2M_CHECKMD5_HEADER, data

    @content_data.byte
    @challenge = @content_data.long
  end

end
