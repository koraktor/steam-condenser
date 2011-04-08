# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'socket'

# This module is included by all classes implementing server functionality
#
# It provides basic name resolution features.
module Server

  # Creates a new server instance with the given address and port
  def initialize(address, port = nil)
    address = address.to_s
    address, port = address.split(':', 2) if address.include? ':'

    @host_names   = []
    @ip_addresses = []
    @ip_index     = 0
    @port         = port.to_i

    Socket.getaddrinfo(address, port, Socket::AF_INET, Socket::SOCK_DGRAM).
           each do |address|
      @host_names << address[2]
      @ip_addresses << address[3]
    end

    @ip_address = @ip_addresses.first

    init_socket
  end

  # Rotate this server's IP address to the next one in the IP list
  #
  # This method will return +true+, if the IP list reached its end. If the list
  # contains only one IP address, this method will instantly return +true+.
  def rotate_ip
    return true if @ip_addresses.size == 1

    @ip_index = (@ip_index + 1) % @ip_addresses.size
    @ip_address = @ip_addresses[@ip_index]

    init_socket

    @ip_index == 0
  end

  protected

  # Execute an action in the context of this server's current IP address
  #
  # Any failure, i.e. an exception, will cause the IP to rotate to the next IP
  # in the server's IP list and the execution will be repeated for the next IP
  # address. If the IP rotation reaches the end of the list, the exception will
  # be reraised.
  def failsafe(&proc)
    begin
      proc.call
    rescue
      raise $! if rotate_ip
      failsafe &proc
    end
  end

end
