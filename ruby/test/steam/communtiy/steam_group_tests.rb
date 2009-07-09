# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

$:.push File.join(File.dirname(__FILE__), '..', '..', '..', 'lib')

require "steam/community/steam_group"
require "test/unit"

class SteamGroupTests < Test::Unit::TestCase

  def test_bypass_cache
    SteamGroup.clear_cache
    group = SteamGroup.new('valve')
    fetch_time = group.fetch_time
    sleep 1
    group = SteamGroup.new('valve', true, true)
    assert_operator(fetch_time, :<, group.fetch_time)
  end

  def test_cache
    SteamGroup.clear_cache
    group = SteamGroup.new('valve')
    fetch_time = group.fetch_time
    assert_equal(true, SteamGroup.cached?(103582791429521412))
    assert_equal(true, SteamGroup.cached?('valve'))
    sleep 1
    group = SteamGroup.new('valve')
    assert_equal(fetch_time, group.fetch_time)
  end

  def test_case_insensitivity
    SteamGroup.clear_cache
    group = SteamGroup.new('valve', false)
    group2 = SteamGroup.new('Valve', false)
    group3 = SteamGroup.new('VALVE', false)
    assert_equal(true, SteamGroup.cached?('valve'))
    assert_equal(group, group2)
    assert_equal(group, group3)
  end

end
