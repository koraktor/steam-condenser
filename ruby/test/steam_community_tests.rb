# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

$:.push File.join(File.dirname(__FILE__), "../lib")

require "steam/community/steam_id"
require "test/unit"

class SteamCommunityTests < Test::Unit::TestCase
  
  # This test tries to aquire information from a online Steam ID. This test only
  # passes if the parsing of the XML document works
  def test_online_steam_id
    assert_nothing_raised do
      steam_id = SteamId.new "Koraktor"
      p steam_id
      p steam_id.get_game_stats "TF2"
    end
  end
  
end
