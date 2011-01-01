# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# The S2M_HEARTBEAT2 packet type is used to signal a game servers availability
# and status to the master servers.
class S2M_HEARTBEAT2_Packet

  include SteamPacket

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

  # Creates a new heartbeat packet to send to a master server
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

end
