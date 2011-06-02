require 'bundler'

require File.expand_path(File.dirname(__FILE__) + '/lib/steam-condenser/version')

Gem::Specification.new do |s|
  s.name        = 'steam-condenser'
  s.version     = SteamCondenser::VERSION
  s.platform    = Gem::Platform::RUBY
  s.authors     = [ 'Sebastian Staudt' ]
  s.email       = [ 'koraktor@gmail.com' ]
  s.homepage    = 'http://koraktor.de/steam-condenser'
  s.summary     = 'A source code history analyzer API'
  s.description = 'A multi-language library for querying the Steam Community, Source, GoldSrc servers and Steam master servers'

  s.add_bundler_dependencies

  s.files              = `git ls-files`.split("\n")
  s.test_files         = `git ls-files -- test/*`.split("\n")
  s.require_paths      = [ 'lib' ]
end
