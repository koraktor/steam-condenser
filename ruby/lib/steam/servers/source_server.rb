# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "exceptions/rcon_no_auth_exception"
require "steam/packets/rcon/rcon_auth_request"
require "steam/packets/rcon/rcon_auth_response"
require "steam/packets/rcon/rcon_exec_request"
require "steam/servers/game_server"
require "steam/sockets/rcon_socket"
require "steam/sockets/source_socket"

class SourceServer < GameServer

  # Splits the player status obtained with +rcon status+
  def self.split_player_status(player_status)
    player_data = player_status.match(/# (\d+) "(.*)" (.*)/).to_a[1..-1]
    player_data[2] = player_data[2].split
    player_data.flatten
  end
  
  def initialize(ip_address, port_number = 27015)
    super port_number
    @rcon_socket = RCONSocket.new ip_address, port_number
    @socket = SourceSocket.new ip_address, port_number
  end
  
  def rcon_auth(password)
    @rcon_request_id = rand 2**16
    
    @rcon_socket.send RCONAuthRequest.new(@rcon_request_id, password)
    @rcon_socket.get_reply
    reply = @rcon_socket.get_reply
    
    if reply.request_id == -1
      raise RCONNoAuthException.new
    end
    
    return reply.get_request_id == @rcon_request_id
  end
  
  def rcon_exec(command)
    @rcon_socket.send RCONExecRequest.new(@rcon_request_id, command)
    response_packets = Array.new
    
    begin
      begin
        response_packet = @rcon_socket.get_reply
        raise RCONNoAuthException.new if response_packet.is_a? RCONAuthResponse
        response_packets << response_packet
      end while true
    rescue TimeoutException
      raise $! if response_packets.empty?
    end
    
    response = String.new
    response_packets.each do |packet|
      response << packet.get_response
    end
    
    return response.strip
  end

end