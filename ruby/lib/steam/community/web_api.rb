# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
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
  def self.api_key=(api_key)
    unless api_key.nil? || api_key.match(/^[0-9A-F]{32}$/)
      raise WebApiException.new(:invalid_key)
    end

    @@api_key = api_key
  end

  # Fetches JSON data from Steam Web API using the specified interface, method
  # and version. Additional parameters are supplied via HTTP GET.
  # Data is returned as a JSON-encoded string.
  def json(interface, method, version = 1, params = nil)
    load(:json, interface, method, version, params)
  end

  # Fetches JSON data from Steam Web API using the specified interface, method
  # and version. Additional parameters are supplied via HTTP GET.
  # Data is returned as a Hash containing the JSON data.
  def json!(interface, method, version = 1, params = nil)
    data = json(interface, method, version, params)
    result = JSON.parse(data, { :symbolize_names => true })[:result]

    if result[:status] != 1
      raise WebApiException.new(:status_bad, result[:status], result[:statusDetail])
    end

    result
  end

  # Fetches data from Steam Web API using the specified interface, method and
  # version. Additional parameters are supplied via HTTP GET.
  # Data is returned as a String in the given format (which may be 'json',
  # 'vdf', or 'xml').
  def load(format, interface, method, version = 1, params = nil)
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
      if $!.io.status[0].to_i == 401
        raise WebApiException.new(:unauthorized)
      end

      raise WebApiException.new(:http_error, $!.io.status[0].to_i, $!.io.status[1])
    end
  end

end
