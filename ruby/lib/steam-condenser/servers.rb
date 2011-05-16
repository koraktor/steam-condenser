# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

module SteamCondenser

  require 'steam-condenser/version'

  # This module is used as a wrapper around Steam Condenser's server classes
  #
  # It does not provide any own functionality, but this file is used to easily
  # require classes to interact with servers
  #
  # @author Sebastian Staudt
  module Servers

    require 'steam/servers/goldsrc_server'
    require 'steam/servers/master_server'
    require 'steam/servers/source_server'

  end

end
