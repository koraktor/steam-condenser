class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket < SteamPacket
  def initialize(challenge_number)
    super A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER, challenge_number.unpack("V")[0]
  end
  
  def get_challenge_number
    return @content_data
  end
end
