# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'steam/community/game_inventory'
require 'steam/community/tf2/tf2_item'

# Represents the inventory (aka. Backpack) of a Team Fortress 2 player
class TF2Inventory

  include GameInventory

  @@app_id = 440

  @@item_class = TF2Item

end
