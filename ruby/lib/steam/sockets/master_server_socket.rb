# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/sockets/steam_socket"

class MasterServerSocket < SteamSocket
  
  def get_reply
    self.receive_packet 1500
    
    if @buffer.get_long != -1
      raise Exception.new("Master query response has wrong packet header.")
    end
    
    return SteamPacketFactory.get_packet_from_data(@buffer.get)
  end
  
end