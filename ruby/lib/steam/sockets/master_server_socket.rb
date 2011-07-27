# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/packet_format_exception'
require 'steam/sockets/steam_socket'

# This class represents a socket used to communicate with master servers
#
# @author Sebastian Staudt
class MasterServerSocket

  include SteamSocket

  # Reads a single packet from the socket
  #
  # @raise [PacketFormatException] if the packet has the wrong format
  # @return [SteamPacket] The packet replied from the server
  def reply
    receive_packet 1500

    unless @buffer.long == 0xFFFFFFFF
      raise PacketFormatException, 'Master query response has wrong packet header.'
    end

    SteamPacketFactory.packet_from_data(@buffer.get)
  end

end
