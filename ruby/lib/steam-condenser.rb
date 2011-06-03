# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'bundler'

# This module is used as a wrapper around Steam Condenser's classes
#
# It does not provide any own functionality, but this file is used as an entry
# point when using the gem (i.e. +require 'steam-condenser').
#
# @author Sebastian Staudt
module SteamCondenser

  require 'steam-condenser/community'
  require 'steam-condenser/servers'
  require 'steam-condenser/version'

end
