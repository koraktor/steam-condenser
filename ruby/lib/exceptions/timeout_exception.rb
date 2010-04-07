# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

class TimeoutException < SteamCondenserException

  def initialize
    super 'The operation timed out.'
  end

end
