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
  
  # This test tries to aquire information from a online Steam ID by using the
  # custom URL. This test only passes if the parsing of the XML document works
  def test_steam_id_by_custom_url
    assert_nothing_raised do
      steam_id = SteamId.new "Koraktor"
      p steam_id
      p steam_id.get_game_stats "TF2"
    end
  end

  # This test tries to aquire information from a online Steam ID by using the
  # 64bit numeric SteamID. This test only passes if the parsing of the XML
  # document works
  def test_steam_id_by_steamid_64
    assert_nothing_raised do
      steam_id = SteamId.new 76561197961384956
      p steam_id
      p steam_id.get_game_stats "TF2"
    end
  end
  
end
