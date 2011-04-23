# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'json'
require 'open-uri'

require 'exceptions/web_api_exception'

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
  #
  # [+api_key+] The 128bit API key that has to be requested from
  #             http://steamcommunity.com/dev
  def self.api_key=(api_key)
    unless api_key.nil? || api_key.match(/^[0-9A-F]{32}$/)
      raise WebApiException.new(:invalid_key)
    end

    @@api_key = api_key
  end

  # Fetches JSON data from Steam Web API using the specified interface, method
  # and version. Additional parameters are supplied via HTTP GET.
  # Data is returned as a JSON-encoded string.
  #
  # [+interface+] The Web API interface to call, e.g. +ISteamUser+
  # [+method+]    The Web API method to call, e.g. +GetPlayerSummaries+
  # [+version+]   The API method version to use (default: 1)
  # [+params+]    A Hash of additional parameters to supply via HTTP GET
  def self.json(interface, method, version = 1, params = nil)
    load(:json, interface, method, version, params)
  end

  # Fetches JSON data from Steam Web API using the specified interface, method
  # and version. Additional parameters are supplied via HTTP GET.
  # Data is returned as a Hash containing the JSON data.
  #
  # [+interface+] The Web API interface to call, e.g. +ISteamUser+
  # [+method+]    The Web API method to call, e.g. +GetPlayerSummaries+
  # [+version+]   The API method version to use (default: 1)
  # [+params+]    A Hash of additional parameters to supply via HTTP GET
  def self.json!(interface, method, version = 1, params = nil)
    data = json(interface, method, version, params)
    result = JSON.parse(data, { :symbolize_names => true })[:result]

    status = result[:status]
    if status != 1
      raise WebApiException.new(:status_bad, status, result[:statusDetail])
    end

    result
  end

  # Fetches data from Steam Web API using the specified interface, method and
  # version. Additional parameters are supplied via HTTP GET.
  # Data is returned as a String in the given format.
  #
  # [+format+]    The format to load from the API ('json', 'vdf', or 'xml')
  # [+interface+] The Web API interface to call, e.g. +ISteamUser+
  # [+method+]    The Web API method to call, e.g. +GetPlayerSummaries+
  # [+version+]   The API method version to use (default: 1)
  # [+params+]    A Hash of additional parameters to supply via HTTP GET
  def self.load(format, interface, method, version = 1, params = nil)
    version = version.to_s.rjust(4, '0')
    url = "http://api.steampowered.com/#{interface}/#{method}/v#{version}/"
    params = {} unless params.is_a?(Hash)
    params[:format] = format
    params[:key] = WebApi.api_key

    unless params.nil? && params.empty?
      url += '?' + params.map { |k,v| "#{k}=#{v}" }.join('&')
    end

    begin
      open(url, { :proxy => true }).read
    rescue OpenURI::HTTPError
      status = $!.io.status[0]
      status = [status, ''] unless status.is_a? Array
      raise WebApiException.new(:unauthorized) if status[0].to_i == 401
      raise WebApiException.new(:http_error, status[0].to_i, status[1])
    end
  end

end
