
autoload "A2A_INFO_RequestPacket", "steam/packets/a2a_info_request_packet"
autoload "A2A_PING_RequestPacket", "steam/packets/a2a_ping_request_packet"
autoload "A2A_PLAYER_RequestPacket", "steam/packets/a2a_player_request_packet"
autoload "A2A_RULES_RequestPacket", "steam/packets/a2a_rules_request_packet"
autoload "A2A_SERVERQUERY_GETCHALLENGE_RequestPacket", "steam/packets/a2a_serverquery_getchallenge_request_packet"

require "steam/steam_player"

class GameServer
  def get_ping
    return @ping
  end
  
  def get_players
    return @player_array
  end
  
  def get_rules
    return @rules_hash
  end
  
  def get_server_info
    return @info_hash
  end
  
  def init
    update_ping
    update_server_info
    update_challenge_number
  end

  def update_player_info
    send_request A2A_PLAYER_RequestPacket.new(@challenge_number)
    @player_array = get_reply.get_player_array
  end

  def update_rules_info
    send_request A2A_RULES_RequestPacket.new(@challenge_number)
    @rules_hash = get_reply.get_rules_hash
  end
  
  def update_server_info
    send_request A2A_INFO_RequestPacket.new
    @info_hash = get_reply.get_info_hash
  end
  
  def update_challenge_number
    send_request A2A_SERVERQUERY_GETCHALLENGE_RequestPacket.new
    @challenge_number = get_reply.get_challenge_number    
  end
 
  def update_ping
    send_request A2A_PING_RequestPacket.new
    start_time = Time.now
    get_reply
    end_time = Time.now
    return @ping = (end_time - start_time) * 1000
  end
  
  def initialize(port_number = 27015)
    unless port_number.to_i > 0 and port_number.to_i < 65536
      raise ArgumentError.new("The listening port of the server has to be a number greater than 0 and less than 65536.")
    end
  end

  private

  def get_reply
    @socket.get_reply
  end
  
  def send_request packet
    @socket.send packet
  end
end
