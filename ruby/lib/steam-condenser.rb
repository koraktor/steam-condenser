# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

libdir = File.dirname(__FILE__)
$LOAD_PATH.unshift(libdir) unless $LOAD_PATH.include?(libdir)

module SteamCondenser

  version = YAML.load_file(File.join(File.dirname(__FILE__), '..', 'VERSION.yml'))
  VERSION = "#{version[:major]}.#{version[:minor]}.#{version[:patch]}"

end
