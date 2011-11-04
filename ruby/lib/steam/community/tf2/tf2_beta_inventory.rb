# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/game_inventory'
require 'steam/community/tf2/tf2_item'

# Represents the inventory (aka. Backpack) of a player of the public Team
# Fortress 2 beta
class TF2BetaInventory

  include Cacheable
  cacheable_with_ids :steam_id64

  include GameInventory

  @@app_id = 520

  @@item_class = TF2Item

end
