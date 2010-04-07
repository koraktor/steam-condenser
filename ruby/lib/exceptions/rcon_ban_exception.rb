# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009-2010, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

class RCONBanException < SteamCondenserException

  def initialize
    super 'You have been banned from this server.'
  end

end
