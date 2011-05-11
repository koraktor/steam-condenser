# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This packet class represents a M2S_ISVALIDMD5 response replied by a master
# server
#
# It is used to provide a challenge number to a game server
#
# @author Sebastian Staudt
# @see MasterServer#challenge
class M2C_ISVALIDMD5_Packet

  include SteamPacket

  # Returns the challenge number to use for master server communication
  #
  # @return [Fixnum] The challenge number
  attr_reader :challenge

  # Creates a new M2S_ISVALIDMD5 response object based on the given data
  #
  # @param [String] data The raw packet data replied from the server
  def initialize(data)
    super M2C_ISVALIDMD5_HEADER, data

    @content_data.byte
    @challenge = @content_data.long
  end

end
