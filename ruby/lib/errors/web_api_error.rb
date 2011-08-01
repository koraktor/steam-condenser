# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'errors/steam_condenser_error'

# This error is raised when a Steam Web API request or a related action fails.
# This can have various reasons like an invalid Web API key or a broken
# request.
#
# @author Sebastian Staudt
# @see WebApi
class WebApiError < SteamCondenserError

  # Creates a new `WebApiError` with an error message according to the given
  # `cause`. If this cause is `:status_bad` (which will origin from the Web API
  # itself) or `:http_error` the details about this failed request will be
  # taken from `status_code` and `status_message`.
  #
  # @param [:http_error, :invalid_key, :status_bad, :unauthorized] cause A
  #        symbolic name for the problem which caused this error:
  #
  #        * `:http_error`: An error during the HTTP request itself will result
  #           in an error with this reason.
  #        * `:invalid_key`: This occurs when trying to set a Web API key that
  #           isn't valid, i.e. a 128 bit integer in a hexadecimal string.
  #        * `:status_bad`: This is caused by a successful request that fails
  #          for some Web API internal reason (e.g. an invalid argument).
  #          Details about this failed request will be taken from `status_code`
  #          and `status_message`.
  #        * `:unauthorized`: This happens when a Steam Web API request is
  #          rejected as unauthorized. This most likely means that you did not
  #          specify a valid Web API key using {WebApi.api_key=}. A Web API key
  #          can be obtained from http://steamcommunity.com/dev/apikey.
  #
  #        Other undefined reasons will cause a generic error message.
  # @param [Fixnum] status_code The HTTP status code returned by the Web API
  # @param [String] status_message The status message returned in the response
  def initialize(cause, status_code = nil, status_message = '')
    case cause
      when :http_error then
        message = "The Web API request has failed due to an HTTP error: #{status_message} (status code: #{status_code})."
      when :invalid_key then
        message = 'This is not a valid Steam Web API key.'
      when :status_bad then
        message = "The Web API request failed with the following error: #{status_message} (status code: #{status_code})."
      when :unauthorized then
        message = 'Your Web API request has been rejected. You most likely did not specify a valid Web API key.'
      else
        message = 'An unexpected error occured while executing a Web API request.'
    end

    super message
  end

end
