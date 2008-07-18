require 'rake/rdoctask'
require 'rake/gempackagetask'
require "rubygems"

# Setting the default task
desc "Default: build gem"
task :default => :gem

# Get all ruby source files for packaging and documentation
source_files = Dir.glob(File.join("**", "lib", "**", "*.rb"))

# Parsing info from svn
svn_info = Hash[*`svn info`.split("\n").map {|line| line.split(": ")}.flatten]

# Gem specification
spec = Gem::Specification.new do |s|
  s.name = "steam-condenser"
  s.version = svn_info["Last Changed Rev"]
  s.date = Time.now
 
  s.summary = "A multi-language library for querying Source, GoldSrc servers and Steam master servers"
 
  s.authors = ["Sebastian Staudt"]
  s.email = "koraktor@gmail.com"
  s.homepage = "http://code.google.com/p/steam-condenser/"
  
  s.has_rdoc = true
  s.rdoc_options = ["--main", "README"]
  s.rdoc_options << "--inline-source" << "--charset=UTF-8"
  
  s.files = %w(README Rakefile) + source_files
end

# Create a rake task to build the gem using the specification
Rake::GemPackageTask.new(spec) do |pkg| 
  pkg.need_tar = true 
end

# Create a rake task to build the documentation
desc "Building docs"
Rake::RDocTask.new(:rdoc) do |rdoc|
  rdoc.title = "Steam Condenser documentation"
  rdoc.rdoc_files.include source_files
  rdoc.rdoc_files.include "README"
  rdoc.main = "README"
  rdoc.rdoc_dir = "doc"
  rdoc.options << "--all" << "--line-numbers"
end