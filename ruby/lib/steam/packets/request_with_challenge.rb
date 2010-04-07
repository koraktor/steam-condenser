# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

# This module implements a to_s method for request packets which send a challenge number
module RequestWithChallenge

  # Returns a packed version of the request
  def to_s
    [0xFF, 0xFF, 0xFF, 0xFF, @header_data, @content_data.data.to_i].pack('c5l')
  end

end
