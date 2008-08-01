autoload "ByteBuffer", "byte_buffer"
autoload "SteamPacket", "steam/packets/steam_packet"

# The A2A_INFO_ResponsePacket class represents the response to a A2A_INFO
# request send to the server.
class A2A_INFO_ResponsePacket < SteamPacket
  
  # Creates a A2A_INFO response object based on the data received.
  protected
  def initialize(header, data)
    super header, data
  end
  
  # Returns the hash containing information on the server
  public
  def get_info_hash
    return Hash[
      *instance_variables.map { |var|
        if var != "@content_data" && var != "@header_data"
          [var[1..-1], instance_variable_get(var)]
        end
      }.compact.flatten
    ]
  end
end
