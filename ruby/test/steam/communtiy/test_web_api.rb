# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'helper'
require 'steam/community/web_api'

class TestWebApi < Test::Unit::TestCase

  context 'The Web API' do

    setup do
      WebApi.api_key = '0123456789ABCDEF0123456789ABCDEF'
    end

    should 'allow access to the API key' do
      assert_equal '0123456789ABCDEF0123456789ABCDEF', WebApi.api_key
    end

    should 'allow to set the API key' do
      WebApi.api_key = 'FEDCBA9876543210FEDCBA9876543210'

      assert_equal 'FEDCBA9876543210FEDCBA9876543210', WebApi.api_key
    end

    should 'fail to set an invalid API key' do
      error = assert_raises WebApiError do
        WebApi.api_key = 'test'
      end
      assert_equal 'This is not a valid Steam Web API key.', error.message
    end

    should 'provide easy access to JSON data' do
      WebApi.expects(:get).with :json, 'interface', 'method', 2, { :test => 'param' }

      WebApi.json 'interface', 'method', 2, { :test => 'param' }
    end

    should 'provide easy access to parsed JSON data' do
      data = mock
      WebApi.expects(:json).with('interface', 'method', 2, { :test => 'param' }).
        returns data
      MultiJson.expects(:decode).with(data, { :symbolize_keys => true }).
        returns({ :result => { :status => 1 }})

      assert_equal({ :status => 1 }, WebApi.json!('interface', 'method', 2, { :test => 'param' }))
    end

    should 'raise an error if the parsed JSON data is an error message' do
      data = mock
      WebApi.expects(:json).with('interface', 'method', 2, { :test => 'param' }).
        returns data
      MultiJson.expects(:decode).with(data, { :symbolize_keys => true }).
        returns({ :result => { :status => 2, :statusDetail => 'error' } })

      error = assert_raises WebApiError do
        WebApi.json! 'interface', 'method', 2, { :test => 'param' }
      end
      assert_equal 'The Web API request failed with the following error: error (status code: 2).', error.message
    end

    should 'load data from the Steam Community Web API' do
      data = mock :read => 'data'
      WebApi.expects(:open).with do |url, options|
        options == { :proxy => true } &&
        url.start_with?('http://api.steampowered.com/interface/method/v0002/?') &&
        (url.split('?').last.split('&') & %w{test=param format=json key=0123456789ABCDEF0123456789ABCDEF}).size == 3
      end.returns data

      assert_equal 'data', WebApi.get(:json, 'interface', 'method', 2, { :test => 'param' })
    end

    should 'handle unauthorized access error when loading data' do
      io = mock :status => [401]
      http_error = OpenURI::HTTPError.new '', io
      WebApi.expects(:open).raises http_error

      error = assert_raises WebApiError do
        WebApi.get :json, 'interface', 'method', 2, { :test => 'param' }
      end
      assert_equal 'Your Web API request has been rejected. You most likely did not specify a valid Web API key.', error.message
    end

    should 'handle generic HTTP errors when loading data' do
      io = mock :status => [[404, 'Not found']]
      http_error = OpenURI::HTTPError.new '', io
      WebApi.expects(:open).raises http_error

      error = assert_raises WebApiError do
        WebApi.get :json, 'interface', 'method', 2, { :test => 'param' }
      end
      assert_equal 'The Web API request has failed due to an HTTP error: Not found (status code: 404).', error.message
    end

  end

end
