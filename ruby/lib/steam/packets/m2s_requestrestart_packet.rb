# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This packet class represent a M2S_REQUESTRESTART response replied from a
# master server
#
# It is used to request a game server restart, e.g. when the server is
# outdated.
#
# @author Sebastian Staudt
# @see MasterServer#sendHeartbeat
class M2S_REQUESTRESTART_Packet

  include SteamPacket

  # Returns the challenge number used for master server communication
  #
  # @return [Fixnum] The challenge number
  attr_reader :challenge

  # Creates a new M2S_REQUESTRESTART response object based on the given data
  #
  # @param [String] data The raw packet data replied from the server
  def initialize(data)
    super M2S_REQUESTRESTART_HEADER, data

    @challenge = @content_data.long
  end

end
