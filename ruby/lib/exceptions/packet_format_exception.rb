# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

class PacketFormatException < SteamCondenserException

  def initialize
    super 'The packet data received doesn\'t match the packet format.'
  end

end
