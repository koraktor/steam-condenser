# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'ipaddr'
require 'test/unit'

$:.push File.join(File.dirname(__FILE__), '..', 'lib')

require 'errors/timeout_error'
require 'steam/servers/goldsrc_server'
require 'steam/servers/master_server'
require 'steam/servers/source_server'

class QueryTests < Test::Unit::TestCase

  # This test tries to initialize an invalid GoldSrc server
  def test_invalid_goldsrc_server
    assert_raise SteamCondenser::TimeoutError do
      invalid_server = GoldSrcServer.new IPAddr.new('1.0.0.0')
      invalid_server.ping
    end
  end

  # This test tries to initialize an invalid Source server
  def test_invalid_source_server
    assert_raise SteamCondenser::TimeoutError do
      invalid_server = SourceServer.new IPAddr.new('1.0.0.0')
      invalid_server.ping
    end
  end

  # This test gets a random GoldSrc server from the master server and does a
  # full query on it
  def test_random_goldsrc_server
    assert_nothing_raised do
      master_server = MasterServer.new(*MasterServer::GOLDSRC_MASTER_SERVER)
      servers = master_server.servers MasterServer::REGION_ALL, '\type\d\empty\1\full\1\gamedir\valve'

      assert !servers.empty?, 'Got no servers from master server.'

      server = GoldSrcServer.new(*servers[rand(servers.length)])
      server.init
      server.update_players
      server.update_rules

      print server.to_s
    end
  end

  # This test gets a random Source server from the master server and does a
  # full query on it
  def test_random_source_server
    assert_nothing_raised do
      master_server = MasterServer.new(*MasterServer::SOURCE_MASTER_SERVER)
      servers = master_server.servers MasterServer::REGION_ALL, '\type\d\empty\1\full\1\gamedir\tf'

      assert !servers.empty?, 'Got no servers from master server.'

      server = SourceServer.new(*servers[rand(servers.length)])
      server.init
      server.update_players
      server.update_rules

      print server.to_s
    end
  end

end
