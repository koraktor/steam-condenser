# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

$:.push File.join(File.dirname(__FILE__), "../lib")

require "ipaddr"
require "steam/servers/goldsrc_server"
require "steam/servers/source_server"
require "test/unit"

class RCONTests < Test::Unit::TestCase
  
  # This test tries to run the "status" command over RCON on a GoldSrc server
  def test_long_rcon_goldsrc_server
    assert_nothing_raised do
      server = GoldSrcServer.new IPAddr.new("127.0.0.1")
      server.rcon_auth "test"
      rcon_reply = server.rcon_exec "cvarlist"
      print "#{rcon_reply}\n"

      assert(
        rcon_reply.include?("CvarList ? for syntax"),
        "Did not receive complete cvarlist.")
    end
  end
  
  # This test tries to run the "status" command over RCON on a Source server
  def test_long_rcon_source_server
    assert_nothing_raised do
      server = SourceServer.new IPAddr.new("127.0.0.1")
      if server.rcon_auth "test"
        rcon_reply = server.rcon_exec "cvarlist"
        print "#{rcon_reply}\n"
      end
    end
  end

  # This test tries to run the "status" command over RCON on a GoldSrc server
  def test_short_rcon_goldsrc_server
    assert_nothing_raised do
      server = GoldSrcServer.new IPAddr.new("127.0.0.1")
      server.rcon_auth "test"
      rcon_reply = server.rcon_exec "version"
      print "#{rcon_reply}\n"

      assert(
        rcon_reply.include?("Protocol version") &&
        rcon_reply.include?("Exe version") &&
        rcon_reply.include?("Exe build"),
        "Did not receive correct version response.");
    end
  end
  
  # This test tries to run the "status" command over RCON on a Source server
  def test_short_rcon_source_server
    assert_nothing_raised do
      server = SourceServer.new IPAddr.new("127.0.0.1")
      if server.rcon_auth "test"
        rcon_reply = server.rcon_exec "version"
        print "#{rcon_reply}\n"
      end
    end
  end

end
