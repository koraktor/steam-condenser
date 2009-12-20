# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

# This module is used to mimic the behavior of abstract classes known from other
# object-oriented programming languages
module AbstractClass
  
  # If this module is included in a class:
  # * protect the constructor of the class
  # * make sure every derived class has a constructor calling the super
  #   constructor 
  def self.included(klass)
    klass.module_eval do
      
      class << self 
        protected :new

        # Each derived class gets a constructor calling the super constructor
        def inherited(klass)
          klass.module_eval do
            
            # The constructor for derived classes, simply calling the super
            # constructor
            def self.new(*args)
              super
            end
            
          end
        end
        
      end
      
    end
  end
 
end