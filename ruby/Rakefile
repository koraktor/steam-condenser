# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require 'rake/rdoctask'
require 'rake/gempackagetask'
require 'rubygems'
require 'jeweler'

src_files = Dir.glob(File.join("lib", "**", "*.rb"))
test_files = Dir.glob(File.join("test", "**", "*.rb"))

# Gem specification
Jeweler::Tasks.new do |s|
  s.name = "steam-condenser"
  s.date = Time.now
  s.summary = 'Steam Condenser - A Steam query library'
  s.description = 'A multi-language library for querying the Steam Community, Source, GoldSrc servers and Steam master servers'
  s.authors = ['Sebastian Staudt']
  s.email = 'koraktor@gmail.com'
  s.homepage = 'http://github.com/koraktor/steam-condenser'
  
  s.files = %w(README.md Rakefile LICENSE) + src_files + test_files
  s.rdoc_options = ["--all", "--inline-source", "--line-numbers", "--charset=utf-8", "--webcvs=http://github.com/koraktor/steam-condenser/source/blob/master/ruby/%s"]
end

# Create a rake task +:rdoc+ to build the documentation
desc "Building docs"
Rake::RDocTask.new do |rdoc|
  rdoc.title = "Steam Condenser documentation"
  rdoc.rdoc_files.include ["lib/**/*.rb", "test/**/*.rb", "LICENSE", "README.md"]
  rdoc.main = "README.md"
  rdoc.rdoc_dir = "rdoc"
  rdoc.options = ["--all", "--inline-source", "--line-numbers", "--charset=utf-8", "--webcvs=http://github.com/koraktor/steam-condenser/source/blob/master/ruby/%s"]
end

# Task for cleaning documentation and package directories
desc "Clean documentation and package directories"
task :clean do
  FileUtils.rm_rf "doc"
  FileUtils.rm_rf "pkg"
end
