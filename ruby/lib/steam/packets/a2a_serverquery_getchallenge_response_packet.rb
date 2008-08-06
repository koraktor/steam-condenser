# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.

autoload "SteamPacket", "steam/packets/steam_packet"

# The A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket class represents the response
# to a A2A_SERVERQUERY_GETCHALLENGE request send to the server.
class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket < SteamPacket
  
  # Creates a A2A_SERVERQUERY_GETCHALLENGE response object based on the data received.
  def initialize(challenge_number)
    super A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER, challenge_number
  end
  
  # Returns the challenge number received from the server
  def get_challenge_number
    return @content_data.rewind.get_long
  end
end
