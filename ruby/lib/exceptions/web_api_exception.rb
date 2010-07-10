# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

# This exceptions is raised when a Steam Web API request or a related action
# fails. This can have various reasons.
class WebApiException < SteamCondenserException

  # Creates a new WebApiException with an error message according to the given
  # +cause+. If this cause is +:status_bad+ (which will origin from the Web API
  # itself the details about this failed request will be taken from
  # +status_code+ and +status_message+.
  #
  # * +:invalid_key+:  This occurs when trying to set a Web API key that isn't
  #                    valid, i.e. a 128 bit integer in a hexadecimal string.
  # * +:status_bad+:   This is caused by a succesful request that fails for
  #                    some other reason (e.g. a invalid argument). Details
  #                    about this failed request will be taken from
  #                    +status_code+ and +status_message+.
  # * +:unauthorized+: This happens when a Steam Web API request is rejected as
  #                    unauthorized This most likely means that you did not
  #                    specify a valid Web API key using +WebAPI.api_key=+. A
  #                    Web API key can be obtained from
  #                    http://steamcommunity.com/dev/apikey.
  #
  # Other undefined reasons will cause a generic error message.
  def initialize(cause, status_code = nil, status_message = '')
    case cause
      when :invalid_key then
        message = 'This is not a valid Steam Web API key.'
      when :status_bad then
        message = "The Web API request failed with the following error: #{status_message} (status code: #{status_code})"
      when :unauthorized then
        message = 'Your Web API request has been rejected. You most likely did not specify a valid Web API key.'
      else
        message = 'An unexpected error occured while executing a Web API request.'
    end

    super message
  end

end
