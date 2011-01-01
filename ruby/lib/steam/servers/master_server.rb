# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/a2m_get_servers_batch2_packet'
require 'steam/packets/c2m_checkmd5_packet'
require 'steam/packets/s2m_heartbeat2_packet'
require 'steam/sockets/master_server_socket'

class MasterServer

  GOLDSRC_MASTER_SERVER = 'hl1master.steampowered.com', 27010
  SOURCE_MASTER_SERVER = 'hl2master.steampowered.com', 27011

  REGION_US_EAST_COAST = 0x00
  REGION_US_WEST_COAST = 0x01
  REGION_SOUTH_AMERICA = 0x02
  REGION_EUROPE = 0x03
  REGION_ASIA = 0x04
  REGION_AUSTRALIA = 0x05
  REGION_MIDDLE_EAST = 0x06
  REGION_AFRICA = 0x07
  REGION_ALL = 0xFF

  def initialize(master_server_address, master_server_port)
    @socket = MasterServerSocket.new master_server_address, master_server_port
    @server_array = []
  end

  # Request a challenge number from the master server. This is used for further
  # communication with the master server.
  #
  # Please note that this is NOT needed for finding servers using the +servers+
  # method
  def challenge
    @socket.send C2M_CHECKMD5_Packet.new
    @socket.reply.challenge
  end

  def servers(region_code = MasterServer::REGION_ALL, filters = '')
    update_servers region_code, filters if @server_array.empty?
    @server_array
  end

  def update_servers(region_code, filters)
    fail_count     = 0
    finished       = false
    current_server = '0.0.0.0:0'

    begin
      @socket.send A2M_GET_SERVERS_BATCH2_Packet.new(region_code, current_server, filters)
      begin
        servers = @socket.reply.servers
        servers.each do |server|
          if server == '0.0.0.0:0'
            finished = true
          else
            current_server = server
            @server_array << server.split(':')
          end
        end
        fail_count = 0
      rescue TimeoutException
        raise $! if (fail_count += 1) == 3
      end
    end while !finished
  end

  # Sends a constructed heartbeat to the master server
  #
  # This can be used to check server versions externally.
  #
  # The reply from the master server – usually zero or more packets. Zero means
  # either the heartbeat was accepted by the master or there was a timeout. So
  # usually it's best to repeat a heartbeat a few times when not receiving any
  # packets.
  def send_heartbeat(data)
    @socket.send S2M_HEARTBEAT2_Packet.new(data)

    reply_packets = []
    begin
      loop { reply_packets << @socket.reply }
    rescue TimeoutException
    end

    reply_packets
  end

end
