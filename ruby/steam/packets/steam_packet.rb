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
  
  def self.create_packet(raw_data)
    header = raw_data[0].to_i;
    data = raw_data[1, raw_data.size];
    
    case header
      when SteamPacket::A2A_INFO_REQUEST_HEADER
        return A2A_INFO_RequestPacket.new;
      when SteamPacket::A2A_INFO_RESPONSE_HEADER
        return A2A_INFO_ResponsePacket.new(data)
      when SteamPacket::A2A_PING_REQUEST_HEADER
        return A2A_PING_RequestPacket.new
      when SteamPacket::A2A_PING_RESPONSE_HEADER
        return A2A_PING_ResponsePacket.new(data)
      when SteamPacket::A2A_PLAYER_REQUEST_HEADER
        return A2A_PLAYER_ResponsePacket.new
      when SteamPacket::A2A_PLAYER_RESPONSE_HEADER
        return A2A_PLAYER_ResponsePacket.new(data)
      when SteamPacket::A2A_RULES_REQUEST_HEADER
        return A2A_RULES_RequestPacket
      when SteamPacket::A2A_RULES_RESPONSE_HEADER
        return A2A_RULES_ResponsePacket.new(data)
      when SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER
        return A2A_SERVERQUERY_GETCHALLENGE_RequestPacket.new
      when SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER
        return A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket.new(data)
      else
        raise Exception.new("Unknown packet with header 0x#{header.to_s 16} received.")
    end
  end
  
  def initialize(header_data, content_data = nil)
    @content_data = content_data
    @header_data = header_data
    @split_packet = false
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