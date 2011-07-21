# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/game_inventory'
require 'steam/community/portal2/portal2_item'

# Represents the inventory (a.k.a. Robot Enrichment) of a Portal 2 player
#
# @author Sebastian Staudt
class Portal2Inventory

  include Cacheable
  cacheable_with_ids :steam_id64

  include GameInventory

  # The Steam Application ID of Portal 2
  @@app_id = 620

  # The class representing Portal 2 items
  @@item_class = Portal2Item

end
