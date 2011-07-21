# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

# This module implements a method to generate raw packet data used by request
# packets which send a challenge number
#
# @author Sebastian Staudt
module RequestWithChallenge

  # Returns the raw data representing this packet
  #
  # @return [String] A string containing the raw data of this request packet
  def to_s
    [0xFF, 0xFF, 0xFF, 0xFF, @header_data, @content_data.string.to_i].pack('c5l')
  end

end
