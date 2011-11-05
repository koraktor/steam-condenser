require File.expand_path(File.dirname(__FILE__) + '/lib/steam-condenser/version')

Gem::Specification.new do |s|
  s.name        = 'steam-condenser'
  s.version     = SteamCondenser::VERSION
  s.platform    = Gem::Platform::RUBY
  s.authors     = [ 'Sebastian Staudt' ]
  s.email       = [ 'koraktor@gmail.com' ]
  s.homepage    = 'http://koraktor.de/steam-condenser'
  s.summary     = 'Steam Condenser - A Steam query library'
  s.description = 'A multi-language library for querying the Steam Community, Source, GoldSrc servers and Steam master servers'

  s.add_dependency 'bzip2-ruby', '~> 0.2.7'
  s.add_dependency 'multi_json', '~> 1.0.3'

  s.add_development_dependency 'mocha', '~> 0.10.0'
  s.add_development_dependency 'rake', '~> 0.9.2'
  s.add_development_dependency 'shoulda-context', '~> 1.0.0'
  s.add_development_dependency 'yard', '~> 0.7.2'

  s.files              = Dir.glob '**/*'
  s.test_files         = Dir.glob 'test/**/*'
  s.require_paths      = [ 'lib' ]
end
