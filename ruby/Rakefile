# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'rake/rdoctask'
require 'rake/gempackagetask'
require 'rubygems'

src_files = Dir.glob(File.join("lib", "**", "*.rb"))
test_files = Dir.glob(File.join("test", "**", "*.rb"))

begin
  require 'ore/tasks'
  Ore::Tasks.new
rescue LoadError
  $stderr.puts 'You need ore-tasks to build the gem. Install it using `gem install ore-tasks`.'
end

# Create a rake task +:rdoc+ to build the documentation
desc "Building docs"
Rake::RDocTask.new do |rdoc|
  rdoc.title = "Steam Condenser documentation"
  rdoc.rdoc_files.include ["lib/**/*.rb", "LICENSE", "README.md"]
  rdoc.main = "README.md"
  rdoc.rdoc_dir = "rdoc"
  rdoc.options = ["--all", "--inline-source", "--line-numbers", "--charset=utf-8", "--webcvs=http://github.com/koraktor/steam-condenser/blob/master/ruby/%s"]
end

# Task for cleaning documentation and package directories
desc "Clean documentation and package directories"
task :clean do
  FileUtils.rm_rf "doc"
  FileUtils.rm_rf "pkg"
end
