# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This class represents a S2M_HEARTBEAT2 packet sent by game servers to master
# servers
#
# It is used to signal the game server's availability and status to the master
# servers.
#
# @author Sebastian Staudt
# @see MasterServer#send_heartbeat
class S2M_HEARTBEAT2_Packet

  include SteamPacket

  # Default data to send with a S2M_HEARTBEAT2 packet
  DEFAULT_DATA = {
    :appid     => 320,
    :bots      => 0,
    :challenge => nil,
    :dedicated => 0,
    :gamedir   => 'hl2mp',
    :gameport  => 27015,
    :gametype  => 'ctf',
    :lan       => 1,
    :map       => 'null',
    :max       => 24,
    :os        => 'l',
    :password  => 0,
    :players   => 0,
    :product   => 'hl2dm',
    :protocol  => 7,
    :region    => 255,
    :secure    => 0,
    :specport  => 0,
    :type      => 'd',
    :version   => '1.0.0.0'
  }

  # Creates a new S2M_HEARTBEAT2 packet object based on the given data
  #
  # @param [Hash<Symbol, Object>] data The data to send with the heartbeat. The
  #        data contents are merge with the values from {DEFAULT_DATA}.
  # @raise [SteamCondenserException] when the required challenge number is
  #        missing
  def initialize(data = {})
    data = DEFAULT_DATA.merge data

    if data[:challenge].nil?
      raise SteamCondenserException.new 'You have to provide a challenge number when sending a heartbeat to a master server.'
    end

    bytes = 0x0A.chr
    data.each { |k, v| bytes += "\\#{k}\\#{v}" }
    bytes += 0x0A.chr

    super S2M_HEARTBEAT2_HEADER, bytes
  end

  # Returns the raw data representing this packet
  #
  # @return [String] A string containing the raw data of this request packet
  def to_s
    [@header_data, @content_data.string].pack('ca*')
  end

end
