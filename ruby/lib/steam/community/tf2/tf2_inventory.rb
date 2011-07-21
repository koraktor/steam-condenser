# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'steam/community/game_inventory'
require 'steam/community/tf2/tf2_item'

# Represents the inventory (aka. Backpack) of a Team Fortress 2 player
#
# @author Sebastian Staudt
class TF2Inventory

  include Cacheable
  cacheable_with_ids :steam_id64

  include GameInventory

  # The Steam Application ID of Team Fortress 2
  @@app_id = 440

  # The class representing Team Fortress 2 items
  @@item_class = TF2Item

end
