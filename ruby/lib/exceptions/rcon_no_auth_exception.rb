# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

class RCONNoAuthException < Exception
  
  def initialize
    super "Not authenticated yet."
  end
  
end