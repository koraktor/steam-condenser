# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/steam_packet'

class RCONGoldSrcRequest < SteamPacket

  def initialize(request)
    super 0x00, request
  end

  def to_s
    [0xFFFFFFFF, @content_data.string].pack('Va*')
  end

end
