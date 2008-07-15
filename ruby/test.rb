$:.push File.dirname(__FILE__)

autoload "A2A_PING_RequestPacket", "steam/packets/a2a_ping_request_packet"
autoload "A2A_INFO_ResponsePacket", "steam/packets/a2a_info_response_packet"
autoload "IPAddr", "ipaddr"
autoload "CondenserSocket", "condenser_socket"
autoload "SourceServer", "steam/source_server"
autoload "SteamPacket", "steam/packets/steam_packet"
autoload "SteamSocket", "steam/steam_socket"

server = SourceServer.new IPAddr.new("84.45.77.22"), 27045
server.init
server.get_rules_info

puts server.inspect