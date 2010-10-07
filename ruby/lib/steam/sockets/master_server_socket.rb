# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/sockets/steam_socket'

class MasterServerSocket

  include SteamSocket

  def reply
    receive_packet 1500

    raise Exception.new("Master query response has wrong packet header.") unless @buffer.long == 0xFFFFFFFF

    SteamPacketFactory.packet_from_data(@buffer.get)
  end

end
