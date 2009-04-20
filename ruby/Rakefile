# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require 'rake/rdoctask'
require 'rake/gempackagetask'
require 'rubygems'
require 'jeweler'

src_files = Dir.glob(File.join("lib", "**", "*.rb"))
test_files = Dir.glob(File.join("test", "**", "*.rb"))

rdoc_options = ["--all", "--inline-source", "--line-numbers", "--charset=utf-8", "--webcvs=http://github.com/koraktor/steam-condenser/source/blob/master/ruby/%s"]

# Gem specification
Jeweler::Tasks.new do |s|
  s.name = "steam-condenser"
  s.date = Time.now
  s.description = s.summary = 'A multi-language library for querying Source, GoldSrc servers and Steam master servers'
  s.authors = ['Sebastian Staudt']
  s.email = 'koraktor@gmail.com'
  s.homepage = 'http://github.com/koraktor/steam-condenser'
  
  s.has_rdoc = true
  s.rdoc_options = rdoc_options
  s.extra_rdoc_files = %w(README Rakefile LICENSE)
  
  s.files = %w(README Rakefile LICENSE) + src_files + test_files
end

# Create a rake task +:rdoc+ to build the documentation
desc "Building docs"
Rake::RDocTask.new(:rdoc) do |rdoc|
  rdoc.title = "Steam Condenser documentation"
  rdoc.rdoc_files.include ["lib/**/*.rb", "test/**/*.rb", "LICENSE", "README"]
  rdoc.main = "README"
  rdoc.rdoc_dir = "doc"
  rdoc.options = rdoc_options
end

# Task for cleaning documentation and package directories
desc "Clean documentation and package directories"
task :clean do
  FileUtils.rm_rf "doc"
  FileUtils.rm_rf "pkg"
end
