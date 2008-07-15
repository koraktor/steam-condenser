class SourceServer
  @challenge_number
  @ip_address
  @map_name
  @ping
  @port_number
  @server_name
  @socket
  
  def initialize(ip_address, port_number = 27015)
    unless ip_address.is_a? IPAddr
      raise TypeError("The IP address has to be of type IPAddr")
    end
    
    unless port_number.is_a? Numeric and port_number > 0
      raise TypeError("The listening port of the server has to be a number greater than 0.")
    end
    
    @ip_address = ip_address
    @port_number = port_number
    @socket = SteamSocket.new ip_address, port_number
  end
  
  def init
    get_ping
    get_server_info
    get_challenge_number
  end
  
  def get_rules_info
    
  end
  
  private
  
  def get_ping
    send_request A2A_PING_RequestPacket.new
    start_time = Time.now
    get_reply
    end_time = Time.now
    return @ping = (end_time - start_time) * 1000
  end
  
  def get_reply
    @socket.get_reply
  end
  
  def get_server_info
    
  end
  
  def get_challenge_number
    
  end
  
  def send_request packet
    @socket.send packet
  end
end