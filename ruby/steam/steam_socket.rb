class SteamSocket < CondenserSocket
  @ip_address
  @read_buffer
  @socket
  
  def get_reply
    self.read_to_buffer 1400
    
    split_packet_header = self.get_long
    
    if split_packet_header == -2
      request_id = self.get_long
      packet_number = self.get_byte
      packet_count = self.get_byte
      split_size = self.get_short
      split_packets[packet_number] = @read_buffer
    end
    
    reply_data = self.read_from_buffer 1400
    
    if reply_data.size > 10
      packet_data = reply_data.unpack("VVccvVca*")
      
      if packet_data[0] == -2
        header = packet_data[6]
        split_packets[packet_data[3]] = packet_data[7]
        
        while(packet_data[3] < packet_data[2] - 1)
          packet_data = @socket.recvfrom 1400
          packet_data = reply_data.unpack("VVccva*")
          split_packets[packet_data[3]] = packet_data[5]
        end
        
        reply_data, sender_sockaddr = (header + split_packet.join("\0")).pack("Vca*")
      end
    end
    
    packet_data = reply_data.unpack("Vca*")
    
    puts packet_data.inspect
    
    case packet_data[1]
      when SteamPacket::A2A_INFO_REQUEST_HEADER then
        reply_packet = A2A_INFO_RequestPacket.new
      when SteamPacket::A2A_INFO_RESPONSE_HEADER then
        reply_packet = A2A_INFO_ResponsePacket.read_from_socket self
      when SteamPacket::A2A_PING_RESPONSE_HEADER then
        reply_packet = A2A_PING_ResponsePacket.new(packet_data[2])
      else
        raise Exception.new("Unknown packet with header 0x#{packet_data[1].to_s(16)} received.")
    end
    
    puts "Received reply packet of type \"#{reply_packet.class.to_s}\""
    
    return reply_packet    

=begin
    if(strlen($replyData) > 10)
    {
      $packetData = unpack("VsplitPacket/VrequestId/cpacketCount/cpacketNumber/vsplitSize/VpacketStart/cpacketHeader/a*packetContent", $replyData);
      if($packetData["splitPacket"] == -2)
      { 
        $header = $packetData["packetHeader"];
        $splitPackets[$packetData["packetNumber"]] = $packetData["packetContent"];
      
        while($packetData["packetNumber"] < $packetData["packetCount"] - 1)
        {
          $packetData = parent::getReply(1400);
          $packetData = unpack("VsplitPacket/VrequestId/cpacketCount/cpacketNumber/vsplitSize/a*packetContent", $packetData);
          $splitPackets[$packetData["packetNumber"]] = $packetData["packetContent"];
        }
  
        $replyData = pack("Vca*", -1, $header, implode("\0", $splitPackets));
      }
    }
    
    $packetData = unpack("VpacketStart/cpacketHeader/a*packetContent", $replyData);
    
    switch($packetData["packetHeader"])
    {
      case SteamPacket::A2A_INFO_REQUEST_HEADER:
        return new A2A_INFO_RequestPacket();
        break;
        
      case SteamPacket::A2A_INFO_RESPONSE_HEADER:
        return new A2A_INFO_ResponsePacket($packetData["packetContent"]);
        break;
      
      case SteamPacket::A2A_PING_REQUEST_HEADER:
        return new A2A_PING_RequestPacket();
        break;
        
      case SteamPacket::A2A_PING_RESPONSE_HEADER:
        return new A2A_PING_ResponsePacket($packetData["packetContent"]);
        break;
        
      case SteamPacket::A2A_PLAYER_REQUEST_HEADER:
        return new A2A_PLAYER_ResponsePacket();
        break;
      
      case SteamPacket::A2A_PLAYER_RESPONSE_HEADER:
        return new A2A_PLAYER_ResponsePacket($packetData["packetContent"]);
        break;
        
      case SteamPacket::A2A_RULES_REQUEST_HEADER:
        return new A2A_RULES_RequestPacket();
        break;
      
      case SteamPacket::A2A_RULES_RESPONSE_HEADER:
        return new A2A_RULES_ResponsePacket($packetData["packetContent"]);
        break;
        
      case SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER:
        return new A2A_SERVERQUERY_GETCHALLENGE_RequestPacket();
        break;
        
      case SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER:
        return new A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket($packetData["packetContent"]);
        break;
        
      default:
        throw new Exception("Unknown packet with header 0x" . dechex($packetData["packetHeader"]) . " received.");
    }
=end
end
  
end