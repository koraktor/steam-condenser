# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'json'
require 'steam/community/steam_id'
require 'steam/community/web_api'

# Represents the special Team Fortress 2 item Golden Wrench. It includes the
# ID of the item, the serial number of the wrench, a reference to the SteamId
# of the owner and the date this player crafted the wrench
class TF2GoldenWrench

  attr_reader :date, :id, :number, :owner

  @@golden_wrenches = nil

  # Returns an array of all golden wrenches (as instances of TF2GoldenWrench)
  def self.golden_wrenches
    if @@golden_wrenches.nil?
      @@golden_wrenches = []

      data = JSON.parse(WebApi.json('ITFItems_440', 'GetGoldenWrenches'), { :symbolize_names => true })
      data[:results][:wrenches][:wrench].each do |wrench_data|
        @@golden_wrenches << TF2GoldenWrench.new(wrench_data)
      end
    end

    @@golden_wrenches
  end

  private

  # Creates a new instance of TF2GoldenWrench with the given data
  def initialize(wrench_data)
    @date   = Time.at(wrench_data[:timestamp])
    @id     = wrench_data[:itemID]
    @number = wrench_data[:wrenchNumber]
    @owner  = SteamId.new(wrench_data[:steamID], false)
  end

end
