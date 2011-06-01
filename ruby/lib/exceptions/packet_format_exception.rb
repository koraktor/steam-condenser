# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

# This exception class indicates a problem when parsing packet data from the
# responses received from a game or master server
#
# @author Sebastian Staudt
class PacketFormatException < SteamCondenserException

  # Creates a new `PacketFormatException` instance
  #
  # @param [String] message The message to attach to the exception
  def initialize(message)
    super message
  end

end
