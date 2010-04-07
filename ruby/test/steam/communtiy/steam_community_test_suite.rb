# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009-2010, Sebastian Staudt

require 'test/unit'

suite_dir = File.dirname(__FILE__)
$LOAD_PATH.unshift(suite_dir) unless $LOAD_PATH.include?(suite_dir)

require 'steam_group_tests'
require 'steam_id_tests'
