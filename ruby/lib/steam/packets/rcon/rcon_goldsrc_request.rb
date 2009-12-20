# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/packets/steam_packet"

class RCONGoldSrcRequest < SteamPacket
  
  def initialize(request)
    super 0x00, request
  end
  
  def to_s
    return [0xFFFFFFFF, @content_data.array].pack("Va*")
  end
  
end