# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This module is included by all classes representing a packet used by Source's
# RCON protocol
#
# It provides a basic implementation for initializing and serializing such a
# packet.
#
# @author Sebastian Staudt
# @see RCONPacketFactory
module RCONPacket

  include SteamPacket

  # Header for authentication requests
  SERVERDATA_AUTH = 3

  # Header for replies to authentication attempts
  SERVERDATA_AUTH_RESPONSE = 2

  # Header for command execution requests
  SERVERDATA_EXECCOMMAND = 2

  # Header for packets with the output of a command execution
  SERVERDATA_RESPONSE_VALUE = 0

  # Returns the request ID used to identify the RCON communication
  #
  # @return [Fixnum] The request ID used to identify the RCON communication
  attr_reader :request_id

  # Creates a new RCON packet object with the given request ID, type and
  # content data
  #
  # @param [Fixnum] request_id The request ID for the current RCON
  #        communication
  # @param [Fixnum] rcon_header The header for the packet type
  # @param [String] rcon_data The raw packet data
  def initialize(request_id, rcon_header, rcon_data)
    super rcon_header, "#{rcon_data}\0\0"

    @request_id = request_id
  end

  # Returns the raw data representing this packet
  #
  # @return [String] A string containing the raw data of this RCON packet
  def to_s
    [@content_data.length + 8, @request_id, @header_data, @content_data.string].pack('V3a*')
  end

end
