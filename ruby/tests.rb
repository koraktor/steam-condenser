# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

$:.push File.join(File.dirname(__FILE__), "lib")

require "exceptions/timeout_exception"
require "ipaddr"
require "steam/servers/goldsrc_server"
require "steam/servers/master_server"
require "steam/servers/source_server"
require "test/unit"

def debug(debug_string)
  if $-v
    puts "DEBUG: #{debug_string}"
  end
end

class Tests < Test::Unit::TestCase
  
  def test_invalid_goldsrc_server
    assert_raise TimeoutException do
      invalidServer = GoldSrcServer.new IPAddr.new("1.0.0.0")
      invalidServer.get_ping
    end
  end
  
  def test_invalid_source_server
    assert_raise TimeoutException do
      invalidServer = SourceServer.new IPAddr.new("1.0.0.0")
      invalidServer.get_ping
    end
  end
  
  def test_random_goldsrc_server
    assert_nothing_raised do
      master_server = MasterServer.new MasterServer::GOLDSRC_MASTER_SERVER
      servers = master_server.get_servers
      server = GoldSrcServer.new *servers[rand(servers.length)]
      server.init
      server.update_player_info
      server.update_rules_info
      
      print server.to_s
    end
  end
  
  def test_random_source_server
    assert_nothing_raised do
      master_server = MasterServer.new MasterServer::SOURCE_MASTER_SERVER
      servers = master_server.get_servers
      server = SourceServer.new *servers[rand(servers.length)]
      server.init
      server.update_player_info
      server.update_rules_info
      
      print server.to_s
    end
  end
  
  def test_rcon_goldsrc_server
    assert_nothing_raised do
      server = GoldSrcServer.new IPAddr.new("192.168.0.2")
      server.rcon_auth "test"
      rcon_reply = server.rcon_exec "status"
      print "#{rcon_reply}\n"
    end
  end
  
  def test_rcon_source_server
    assert_nothing_raised do
      server = SourceServer.new IPAddr.new("127.0.0.1")
      if server.rcon_auth "test"
        rcon_reply = server.rcon_exec "status"
        print "#{rcon_reply}\n"
      end
    end
  end
  
end
