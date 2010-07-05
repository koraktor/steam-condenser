# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'open-uri'

require 'exceptions/steam_condenser_exception'

# This adds support for Steam Web API to classes needing this functionality.
# The Web API requires you to register a domain with your Steam account to
# acquire an API key. See http://steamcommunity.com/dev for further details.
module WebApi

  @@api_key = nil

  # Returns the Steam Web API key
  def self.api_key
    @@api_key
  end

  # Sets the Steam Web API key.
  def self.api_key=(api_key)
    unless api_key.nil? || api_key.match(/[0-9A-F]{32}/)
      raise SteamCondenserException.new('This is not a valid Steam Web API key.')
    end

    @@api_key = api_key
  end

  # Fetches JSON data from Steam Web API using the specified interface, method
  # and version. Additional parameters are supplied via HTTP GET.
  def json(interface, method, version, params = nil)
    fetch(:json, interface, method, version, params)
  end

  # Fetches data from Steam Web API using the specified interface, method and
  # version. Additional parameters are supplied via HTTP GET.
  # Data returned has the given format (which may be 'json', 'vdf', 'xml').
  def fetch(format, interface, method, version, params = nil)
    version = version.to_s.rjust(4, '0')
    url = "http://api.steampowered.com/#{interface}/#{method}/v#{version}/?key=#{WebApi.api_key}&format=#{format}"
    url += "&#{params}" unless params.nil?
    open(url, { :proxy => true }).read
  end

end
