# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/steam_packet'

class M2A_SERVER_BATCH_Packet

  include SteamPacket

  attr_reader :servers

  def initialize(data)
    super M2A_SERVER_BATCH_HEADER, data

    unless @content_data.byte == 0x0A
      raise PacketFormatException.new('Master query response is missing additional 0x0A byte.')
    end

    @servers = []

    begin
      first_octet = @content_data.byte
      second_octet = @content_data.byte
      third_octet = @content_data.byte
      fourth_octet = @content_data.byte
      port_number = @content_data.short
      port_number = ((port_number & 0xFF) << 8) + (port_number >> 8)

      @servers << "#{first_octet}.#{second_octet}.#{third_octet}.#{fourth_octet}:#{port_number}"
    end while @content_data.remaining > 0
  end

end
