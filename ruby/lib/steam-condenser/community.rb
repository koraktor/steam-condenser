# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

module SteamCondenser

  require 'steam-condenser/version'

  # This module is used as a wrapper around Steam Condenser's Steam Community
  # classes
  #
  # It does not provide any own functionality, but this file is used to easily
  # require classes to interact with the Steam Community
  #
  # @author Sebastian Staudt
  module Community

    require 'steam/community/steam_id'
    require 'steam/community/web_api'

  end

end
