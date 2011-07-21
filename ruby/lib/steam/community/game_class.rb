# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

# A module implementing basic functionality for classes representing player
# classes
#
# @author Sebastian Staudt
module GameClass

  # Returns the name of this class
  #
  # @return [String] The name of this class
  attr_reader :name

  # Returns the time in minutes the player has played with this class
  #
  # @return [Fixnum] The time this class has been played
  attr_reader :play_time

end
