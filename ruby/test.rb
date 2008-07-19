#!/usr/bin/env ruby

$:.push File.join(File.dirname(__FILE__), "lib")

require "ipaddr"
require "steam/source_server"

def debug(debug_string)
  if $-v
    puts "DEBUG: #{debug_string}"
  end
end

server = SourceServer.new IPAddr.new("84.45.77.22"), 27045
server.init
server.get_player_info
server.get_rules_info

p server
