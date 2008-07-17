#!/usr/bin/env ruby

# Includes all files in lib/ for autoloading
# Respects special file name format for the A2A packets 
Dir.glob(File.join("**", "lib", "**", "*.rb")).each do |class_file|
  class_name = ""
  class_name_array = File.basename(class_file, ".rb").split("_")
  
  if class_name_array[0] == "a2a"
    while class_name_array.size > 2
      class_name << "#{class_name_array.shift.upcase}_"
    end
  end

  class_name << class_name_array.map {|s| s.capitalize}.join
  autoload class_name, class_file
end

autoload "IPAddr", "ipaddr"

def debug(debug_string)
  if $-v
    puts "DEBUG: #{debug_string}"
  end
end

server = SourceServer.new IPAddr.new("84.45.77.22"), 27045
server.init
server.get_player_info
server.get_rules_info

p server
