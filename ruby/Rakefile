# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

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

# Check if YARD is installed
begin
  require 'yard'

  # Create a rake task +:doc+ to build the documentation using YARD
  YARD::Rake::YardocTask.new do |yardoc|
    yardoc.name    = 'doc'
    yardoc.files   = [ 'lib/**/*.rb', 'LICENSE', 'README.md' ]
    yardoc.options = [ '--private', '--title', 'Metior — API Documentation' ]
  end
rescue LoadError
  # Create a rake task +:doc+ to show that YARD is not installed
  desc 'Generate YARD Documentation (not available)'
  task :doc do
    $stderr.puts 'You need YARD to build the documentation. Install it using `gem install yard`.'
  end
end

# Task for cleaning documentation and package directories
desc "Clean documentation and package directories"
task :clean do
  FileUtils.rm_rf "doc"
  FileUtils.rm_rf "pkg"
end
