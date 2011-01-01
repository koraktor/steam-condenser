# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# The C2M_CHECKMD5 packet type is used to initialize (challenge) master server
# communication.
class C2M_CHECKMD5_Packet

  include SteamPacket

  # Creates a new challenge request packet for master server communication
  def initialize
    super C2M_CHECKMD5_HEADER
  end

  # Returns a byte array representation of the packet data
  def to_s
    [@header_data, 0xFF].pack('c2')
  end

end
