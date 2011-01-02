# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# A packet of type M2S_ISVALIDMD5 is used by the master server to provide a
# challenge number to a game server
class M2C_ISVALIDMD5_Packet

  include SteamPacket

  # Returns the challenge number to use for master server communication
  attr_reader :challenge

  # Creates a new response packet with the data from the master server
  def initialize(data)
    super M2C_ISVALIDMD5_HEADER, data

    @content_data.byte
    @challenge = @content_data.long
  end

end
