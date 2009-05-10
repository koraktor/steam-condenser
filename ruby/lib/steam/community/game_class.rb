# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

require 'abstract_class'

# Super class for classes representing player classes
class GameClass

  include AbstractClass

  attr_reader :name, :play_time

end
