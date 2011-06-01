# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

# This exception class indicates that an operation could not be finished within
# a reasonable amount of time
#
# This usually indicates that a server could not be contacted because of
# network problems.
#
# @author Sebastian Staudt
# @note {SteamSocket.timeout=} allows to set a custom timeout for socket
#       operations
class TimeoutException < SteamCondenserException

  # Creates a new `TimeoutException` instance
  def initialize
    super 'The operation timed out.'
  end

end
