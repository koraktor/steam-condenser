# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

$:.push File.join(File.dirname(__FILE__), '..', '..', '..', 'lib')

require "steam/community/steam_id"
require "test/unit"

class SteamIdTests < Test::Unit::TestCase

  def test_bypass_cache
    SteamId.clear_cache
    steam_id = SteamId.new('koraktor')
    fetch_time = steam_id.fetch_time
    sleep 1
    steam_id = SteamId.new('koraktor', true, true)
    assert_operator(fetch_time, :<, steam_id.fetch_time)
  end

  def test_cache
    SteamId.clear_cache
    steam_id = SteamId.new('koraktor')
    fetch_time = steam_id.fetch_time
    assert_equal(true, SteamId.cached?(76561197961384956))
    assert_equal(true, SteamId.cached?('koraktor'))
    sleep 1
    steam_id = SteamId.new('koraktor')
    assert_equal(fetch_time, steam_id.fetch_time)
  end

  def test_case_insensitivity
    SteamId.clear_cache
    steam_id = SteamId.new('koraktor', false)
    steam_id2 = SteamId.new('Koraktor', false)
    steam_id3 = SteamId.new('KORAKTOR', false)
    assert_equal(true, SteamId.cached?('koraktor'))
    assert_equal(steam_id, steam_id2)
    assert_equal(steam_id, steam_id3)
  end

  def test_convert_steam_id_to_community_id
    steam_id64 = SteamId.convert_steam_id_to_community_id('STEAM_0:0:12345')
    assert_equal(76561197960290418, steam_id64)
  end

end
