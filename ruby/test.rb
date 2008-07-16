$:.push File.dirname(__FILE__)

autoload "A2A_INFO_RequestPacket", "steam/packets/a2a_info_request_packet"
autoload "A2A_INFO_ResponsePacket", "steam/packets/a2a_info_response_packet"
autoload "A2A_PING_RequestPacket", "steam/packets/a2a_ping_request_packet"
autoload "A2A_PING_ResponsePacket", "steam/packets/a2a_ping_response_packet"
autoload "A2A_PLAYER_RequestPacket", "steam/packets/a2a_player_request_packet"
autoload "A2A_PLAYER_ResponsePacket", "steam/packets/a2a_player_response_packet"
autoload "A2A_RULES_RequestPacket", "steam/packets/a2a_rules_request_packet"
autoload "A2A_RULES_ResponsePacket", "steam/packets/a2a_rules_response_packet"
autoload "A2A_SERVERQUERY_GETCHALLENGE_RequestPacket", "steam/packets/a2a_serverquery_getchallenge_request_packet"
autoload "A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket", "steam/packets/a2a_serverquery_getchallenge_response_packet"
autoload "IPAddr", "ipaddr"
autoload "CondenserSocket", "condenser_socket"
autoload "SourceServer", "steam/source_server"
autoload "SteamPacket", "steam/packets/steam_packet"
autoload "SteamSocket", "steam/steam_socket"

def debug(debug_string)
  if $-v
    puts "DEBUG: #{debug_string}"
  end
end

server = SourceServer.new IPAddr.new("84.45.77.22"), 27045
server.init
server.get_player_info
server.get_rules_info

p server
