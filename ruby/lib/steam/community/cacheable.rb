# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

# This module implements caching functionality to be used in any object class
# having one or more unique object identifier (i.e. ID) and using a `fetch`
# method to fetch data, e.g. using a HTTP download.
#
# @author Sebastian Staudt
module Cacheable

  # When this module is included in another class it is initialized to make use
  # of caching
  #
  # The original `new` method of the including class will be aliased with
  # `create`, relaying all instantiations to the `new` method defined in
  # {ClassMethods}. Additionally the class variable to save the attributes to
  # cache (i.e. cache IDs) and the cache class variable itself are initialized.
  #
  # @param [Class] base The class to extend with caching functionality
  # @see ClassMethods
  def self.included(base)
    base.extend ClassMethods
    base.send :class_variable_set, :@@cache, {}
    base.send :class_variable_set, :@@cache_ids, []

    class << base
      alias_method :create, :new
    end
  end

  module ClassMethods

    # Defines wich instance variables which should be used to index the cached
    # objects
    #
    # @note A call to this method is needed if you want a class including this
    #       module to really use the cache.
    # @param [Array<Symbol>] ids The symbolic names of the instance variables
    #        representing a unique identifier for this object class
    def cacheable_with_ids(*ids)
      class_variable_set(:@@cache_ids, ids)
    end

    # Returns whether an object with the given ID is already cached
    #
    # @param [Object] id The ID of the desired object
    # @return [Boolean] `true` if the object with the given ID is already
    #         cached
    def cached?(id)
      id.downcase! if id.is_a? String
      class_variable_get(:@@cache).key?(id)
    end

    # Clears the object cache for the class this method is called on
    def clear_cache
      class_variable_set :@@cache, {}
    end

    # This checks the cache for an existing object. If it exists it is
    # returned, otherwise a new object is created.
    # Overrides the default `new` method of the cacheable object class.
    #
    # @param [Object] id The ID of the object that should be loaded
    # @param [Boolean] fetch whether the object's data should be retrieved
    # @param [Boolean] bypass_cache whether the object should be loaded again
    #        even if it is already cached
    # @see #cached?
    # @see #fetch
    def new(id, fetch = true, bypass_cache = false)
      if cached?(id) && !bypass_cache
        object = class_variable_get(:@@cache)[id]
        object.fetch if fetch && !object.fetched?
        object
      else
        super(id, fetch)
      end
    end

  end

  # Returns the time the object's data has been fetched the last time
  #
  # @return [Time] The time the object has been updated the last time
  attr_reader :fetch_time

  # Creates a new object and fetches the associated data of the object if
  # desired
  #
  # @note The real constructor of cacheable classes is {ClassMethods#new}.
  # @param [Boolean] fetch_now If `true`, the object's {#fetch} method is
  #        called to retrieve its data
  def initialize(fetch_now = true) #:notnew:
    fetch if fetch_now
    cache
  end

  # Saves this object in the cache
  #
  # This will use the ID attributes selected for caching
  def cache
    cache     = self.class.send :class_variable_get, :@@cache
    cache_ids = self.class.send :class_variable_get, :@@cache_ids

    cache_ids.each do |cache_id|
      cache_id_value = instance_variable_get('@' + cache_id.to_s)
      unless cache_id_value.nil? or cache.key?(cache_id_value)
        cache[cache_id_value] = self
      end
    end

    true
  end

  # Sets the time this object has been fetched the last time
  #
  # @note This method should be overridden in cacheable object classes and
  #       should implement the logic to retrieve the object's data. The
  #       overriding method should always call `super` to have the fetch time
  #       up-to-date.
  def fetch
    @fetch_time = Time.now
  end

  # Returns whether the data for this object has already been fetched
  #
  # @return [Boolean] `true` if this object's data is available
  def fetched?
    !@fetch_time.nil?
  end

end
