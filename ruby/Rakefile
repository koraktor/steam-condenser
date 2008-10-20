# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "rake/rdoctask"
require "rake/gempackagetask"
require "rubygems"

# Setting the default task
desc "Default: build gem"
task :default => :gem

multitask :all => [:rdoc, :gem]

src_files = Dir.glob(File.join("lib", "**", "*.rb"))

# Set language to English
ENV["LANG"] = "en"

# Parsing info from svn
svn_info = Hash[*`svn info`.split("\n").map {|line| line.split(": ")}.flatten]

rdoc_options = ["--all", "--inline-source", "--line-numbers", "--charset=utf-8", "--webcvs=http://code.google.com/p/steam-condenser/source/browse/trunk/ruby/%s?r=#{svn_info["Last Changed Rev"]}"]

# Gem specification
spec = Gem::Specification.new do |s|
  s.name = "steam-condenser"
  s.version = svn_info["Last Changed Rev"]
  s.date = svn_info["Last Changed Date"]
 
  s.summary = "A multi-language library for querying Source, GoldSrc servers and Steam master servers"
 
  s.authors = ["Sebastian Staudt"]
  s.email = "koraktor@gmail.com"
  s.homepage = "http://code.google.com/p/steam-condenser/"
  
  s.has_rdoc = true
  s.rdoc_options = rdoc_options
  s.extra_rdoc_files = %w(README Rakefile LICENSE)
  
  s.files = %w(README Rakefile LICENSE) + src_files
end

# Create a rake task +:gem+ to build the gem using the specification
desc "Building gem"
Rake::GemPackageTask.new(spec) do |pkg|
  pkg.need_tar = true
end

# Create a rake task +:rdoc+ to build the documentation
desc "Building docs"
Rake::RDocTask.new(:rdoc) do |rdoc|
  rdoc.title = "Steam Condenser documentation"
  rdoc.rdoc_files.include "lib/**/*.rb"
  rdoc.rdoc_files.include ["LICENSE", "README", "tests.rb"]
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