# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'socket'

# This module is included by all classes implementing server functionality
#
# It provides basic name resolution features and the ability to rotate between
# different IP addresses belonging to a single DNS name.
#
# @author Sebastian Staudt
module Server

  # Returns a list of host names associated with this server
  #
  # @return [Array<String>] The host names of this server
  attr_reader :host_names

  # Returns a list of IP addresses associated with this server
  #
  # @return [Array<String>] The IP addresses of this server
  attr_reader :ip_addresses

  # Creates a new server instance with the given address and port
  #
  # @param [String] address Either an IP address, a DNS name or one of them
  #        combined with the port number. If a port number is given, e.g.
  #        'server.example.com:27016' it will override the second argument.
  # @param [Fixnum] port The port the server is listening on
  # @see init_socket
  # @raise [SteamCondenserException] if an host name cannot be resolved
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
  # If this method returns `true`, it indicates that all IP addresses have been
  # used, hinting at the server(s) being unreachable. An appropriate action
  # should be taken to inform the user.
  #
  # Servers with only one IP address will always cause this method to return
  # `true` and the sockets will not be reinitialized.
  #
  # @return [Boolean] `true`, if the IP list reached its end. If the list
  #         contains only one IP address, this method will instantly return
  #         `true`
  # @see #init_socket
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
  #
  # @param [Proc] proc The action to be executed in a failsafe way
  # @see #rotate_ip
  def failsafe(&proc)
    begin
      proc.call
    rescue
      raise $! if rotate_ip
      failsafe &proc
    end
  end

  # Initializes the socket(s) to communicate with the server
  #
  # @abstract Must be implemented in including classes to prepare sockets for
  #           server communication
  def init_socket
  end

end
