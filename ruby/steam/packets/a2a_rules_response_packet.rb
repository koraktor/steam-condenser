class A2A_RULES_ResponsePacket < SteamPacket
  # TODO building the rules hash doesn't work correctly
  def initialize(content_data)
    if content_data == nil
      raise Exception.new("Wrong formatted A2A_RULES response packet.")
    end
    
    super SteamPacket::A2A_RULES_RESPONSE_HEADER, content_data
    
    number_of_rules, rules_data = content_data.unpack("sa*")
    rules_data = rules_data.split("\0")
    @rules_hash = Hash.new
    
    for i in (0..rules_data.size)
    	@rules_hash[rules_data[i]] = rules_data[i+1]
    	++i
    end
  end

  def get_rules_hash
    return @rules_hash
  end
end
