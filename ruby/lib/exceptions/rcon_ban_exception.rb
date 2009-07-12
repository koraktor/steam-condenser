# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

class RCONBanException < Exception
  
  def initialize
    super 'You have been banned from this server.'
  end
  
end