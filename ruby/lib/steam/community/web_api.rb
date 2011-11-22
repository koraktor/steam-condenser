# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'multi_json'
require 'open-uri'

require 'errors/web_api_error'

# This module provides functionality for accessing Steam's Web API
#
# The Web API requires you to register a domain with your Steam account to
# acquire an API key. See http://steamcommunity.com/dev for further details.
#
# @author Sebastian
module WebApi

  # Returns the Steam Web API key currently used by Steam Condenser
  #
  # @return [String] The currently active Steam Web API key
  def self.api_key
    @@api_key
  end

  # Sets the Steam Web API key
  #
  # @param [String] api_key The 128bit API key as a hexadecimal string that has
  #        to be requested from http://steamcommunity.com/dev
  # @raise [WebApiError] if the given API key is not a valid 128bit hexadecimal
  #        string
  def self.api_key=(api_key)
    unless api_key.nil? || api_key.match(/^[0-9A-F]{32}$/)
      raise WebApiError, :invalid_key
    end

    @@api_key = api_key
  end

  # Fetches JSON data from Steam Web API using the specified interface, method
  # and version. Additional parameters are supplied via HTTP GET.
  # Data is returned as a JSON-encoded string.
  #
  # @param [String] interface The Web API interface to call, e.g. `ISteamUser`
  # @param [String] method The Web API method to call, e.g.
  #        `GetPlayerSummaries`
  # @param [Fixnum] version The API method version to use
  # @param [Hash<Symbol, Object>] params Additional parameters to supply via
  #        HTTP GET
  # @raise [WebApiError] if the request to Steam's Web API fails
  # @return [String] The raw JSON data replied to the request
  def self.json(interface, method, version = 1, params = nil)
    get(:json, interface, method, version, params)
  end

  # Fetches JSON data from Steam Web API using the specified interface, method
  # and version. Additional parameters are supplied via HTTP GET.
  # Data is returned as a Hash containing the JSON data.
  #
  # @param [String] interface The Web API interface to call, e.g. `ISteamUser`
  # @param [String] method The Web API method to call, e.g.
  #        `GetPlayerSummaries`
  # @param [Fixnum] version The API method version to use
  # @param [Hash<Symbol, Object>] params Additional parameters to supply via
  #        HTTP GET
  # @raise [WebApiError] if the request to Steam's Web API fails
  # @return [Hash<Symbol, Object>] The JSON data replied to the request
  def self.json!(interface, method, version = 1, params = nil)
    data = json(interface, method, version, params)
    result = MultiJson.decode(data, { :symbolize_keys => true })[:result]

    status = result[:status]
    if status != 1
      raise WebApiError.new :status_bad, status, result[:statusDetail]
    end

    result
  end

  # Fetches data from Steam Web API using the specified interface, method and
  # version. Additional parameters are supplied via HTTP GET. Data is returned
  # as a string in the given format.
  #
  # @param [Symbol] format The format to load from the API (`:json`, `:vdf`, or
  #        `:xml`)
  # @param [String] interface The Web API interface to call, e.g. `ISteamUser`
  # @param [String] method The Web API method to call, e.g.
  #        `GetPlayerSummaries`
  # @param [Fixnum] version The API method version to use
  # @param [Hash<Symbol, Object>] params Additional parameters to supply via
  #        HTTP GET
  # @raise [WebApiError] if the request to Steam's Web API fails
  # @return [String] The data as replied by the Web API in the desired format
  def self.get(format, interface, method, version = 1, params = nil)
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
      raise WebApiError, :unauthorized if status[0].to_i == 401
      raise WebApiError.new :http_error, status[0].to_i, status[1]
    end
  end

end
