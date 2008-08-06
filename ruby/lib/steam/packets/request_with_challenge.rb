# This module implements a to_s method for request packets which send a challenge number
module RequestWithChallenge
  
  # Returns a packed version of the request
  def to_s
    return [0xFF, 0xFF, 0xFF, 0xFF, @header_data, @content_data.array.to_i].pack("c5l")
  end
  
end