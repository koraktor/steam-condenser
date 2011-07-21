# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This packet class represents a C2M_CHECKMD5 request sent to a master server
#
# It is used to initialize (challenge) master server communication.
#
# @author Sebastian Staudt
# @see MasterServer#challenge
class C2M_CHECKMD5_Packet

  include SteamPacket

  # Creates a new C2M_CHECKMD5 request object
  def initialize
    super C2M_CHECKMD5_HEADER
  end

  # Returns the raw data representing this packet
  #
  # @return [String] A string containing the raw data of this request packet
  def to_s
    [@header_data, 0xFF].pack('c2')
  end

end
