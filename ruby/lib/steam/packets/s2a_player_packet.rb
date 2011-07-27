# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/packet_format_exception'
require 'steam/packets/steam_packet'

# This class represents a S2A_PLAYER response sent by a game server
#
# It is used to transfer a list of players currently playing on the server.
#
# @author Sebastian Staudt
# @see GameServer#update_player_info
class S2A_PLAYER_Packet

  include SteamPacket

  # Returns the list of active players provided by the server
  #
  # @return [Hash<String, SteamPlayer>] All active players on the server
  attr_reader :player_hash

  # Creates a new S2A_PLAYER response object based on the given data
  #
  # @param [String] content_data The raw packet data sent by the server
  def initialize(content_data)
    if content_data.nil?
      raise PacketFormatException, 'Wrong formatted S2A_PLAYER packet.'
    end

    super S2A_PLAYER_HEADER, content_data

    @content_data.byte
    @player_hash = {}

    while @content_data.remaining > 0
      player_data = @content_data.byte, @content_data.cstring, @content_data.signed_long, @content_data.float
      @player_hash[player_data[1]] = SteamPlayer.new(*player_data[0..3])
    end
  end

end
