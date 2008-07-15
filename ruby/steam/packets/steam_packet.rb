class SteamPacket
  A2A_INFO_REQUEST_HEADER = 0x54
  A2A_INFO_RESPONSE_HEADER = 0x49
  A2A_PING_REQUEST_HEADER = 0x69
  A2A_PING_RESPONSE_HEADER = 0x6A
  A2A_PLAYER_REQUEST_HEADER = 0x55
  A2A_PLAYER_RESPONSE_HEADER = 0x44
  A2A_RULES_REQUEST_HEADER = 0x56
  A2A_RULES_RESPONSE_HEADER = 0x45
  A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER = 0x57
  A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER = 0x41
  
  @content_data
  @header_data
  
  def initialize(header_data, content_data = nil)
    @content_data = content_data
    @header_data = header_data
  end
  
  def to_s
    packet_data = [0xFF, 0xFF, 0xFF].pack("ccc")
    
    unless @split_packet
      packet_data << [0xFF].pack("c")
    else
      packet_data << [0xFE].pack("c")
    end
    
    return packet_data << [@header_data, @content_data].pack("ca*")
  end    
end