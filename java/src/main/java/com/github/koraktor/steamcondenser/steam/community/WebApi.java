/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;

/**
 * This adds support for Steam Web API to classes needing this functionality.
 * The Web API requires you to register a domain with your Steam account to
 * acquire an API key. See http://steamcommunity.com/dev for further details.
 *
 * @author Sebastian Staudt
 */
abstract public class WebApi
{

    private static String apiKey;

    /**
     * Returns the Steam Web API key
     *
     * @return The current Web API key
     */
    public static String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the Steam Web API key.
     *
     * @param apiKey The Web API key to use
     */
    public static void setApiKey(String apiKey)
            throws WebApiException {
        if(apiKey != null && !apiKey.matches("^[0-9A-F]{32}$")) {
            throw new WebApiException(WebApiException.Cause.INVALID_KEY);
        }

        WebApi.apiKey = apiKey;
    }

    /**
     * Fetches JSON data from Steam Web API using the specified interface,
     * method and version. Additional parameters are supplied via HTTP GET.
     * Data is returned as a JSON-encoded string.
     *
     * @param apiInterface The Web API interface to use
     * @param method The Web API method to use
     * @return Data is returned as a JSON-encoded string.
     * @throws WebApiException In case of any request failure
     */
    public static String getJSON(String apiInterface, String method)
            throws WebApiException {
        return load("json", apiInterface, method, 1, null);
    }

    /**
     * Fetches JSON data from Steam Web API using the specified interface,
     * method and version. Additional parameters are supplied via HTTP GET.
     * Data is returned as a JSON-encoded string.
     *
     * @param apiInterface The Web API interface to use
     * @param method The Web API method to use
     * @param version The interface version to use
     * @return Data is returned as a JSON-encoded string.
     * @throws WebApiException In case of any request failure
     */
    public static String getJSON(String apiInterface, String method, int version)
            throws WebApiException {
        return load("json", apiInterface, method, version, null);
    }

    /**
     * Fetches JSON data from Steam Web API using the specified interface,
     * method and version. Additional parameters are supplied via HTTP GET.
     *
     * @param apiInterface The Web API interface to use
     * @param method The Web API method to use
     * @param version The interface version to use
     * @return Data is returned as a JSON-encoded string.
     * @throws WebApiException In case of any request failure
     */
    public static String getJSON(String apiInterface, String method, int version, Map<String, Object> params)
            throws WebApiException {
        return load("json", apiInterface, method, version, params);
    }

    /**
     * Fetches JSON data from Steam Web API using the specified interface,
     * method and version. Additional parameters are supplied via HTTP GET.
     * Data is returned as a Hash containing the JSON data.
     *
     * @param apiInterface The Web API interface to use
     * @param method The Web API method to use
     * @return Data is returned as a <code>JSONObject</code>
     * @throws JSONException In case of misformatted JSON data
     * @throws WebApiException In case of any request failure
     */
    public static JSONObject getJSONData(String apiInterface, String method)
            throws JSONException, WebApiException {
        return getJSONData(apiInterface, method, 1, null);
    }

    /**
     * Fetches JSON data from Steam Web API using the specified interface,
     * method and version. Additional parameters are supplied via HTTP GET.
     * Data is returned as a Hash containing the JSON data.
     *
     * @param apiInterface The Web API interface to use
     * @param method The Web API method to use
     * @param version The interface version to use
     * @return Data is returned as a <code>JSONObject</code>
     * @throws JSONException In case of misformatted JSON data
     * @throws WebApiException In case of any request failure
     */
    public static JSONObject getJSONData(String apiInterface, String method, int version)
            throws JSONException, WebApiException {
        return getJSONData(apiInterface, method, version, null);
    }

    /**
     * Fetches JSON data from Steam Web API using the specified interface,
     * method and version. Additional parameters are supplied via HTTP GET.
     *
     * @param apiInterface The Web API interface to use
     * @param method The Web API method to use
     * @param params The HTTP GET parameters to send with the request
     * @param version The interface version to use
     * @return Data is returned as a <code>JSONObject</code>
     * @throws JSONException In case of misformatted JSON data
     * @throws WebApiException In case of any request failure
     */
    public static JSONObject getJSONData(String apiInterface, String method, int version, Map<String, Object> params)
            throws JSONException, WebApiException {
        String data = getJSON(apiInterface, method, version, params);
        JSONObject result = new JSONObject(data).getJSONObject("result");

        if(result.getInt("status") != 1) {
            throw new WebApiException(WebApiException.Cause.STATUS_BAD, result.getInt("status"), result.getString("statusDetail"));
        }

        return result;
    }

    /**
     * Fetches data from Steam Web API using the specified interface, method and
     * version. Additional parameters are supplied via HTTP GET.
     * Data returned has the given format (which may be 'json', 'vdf', 'xml').
     *
     * @param apiInterface The Web API interface to use
     * @param format The format to use for the response. May be "json", "vdf"
     *               or "xml".
     * @param method The Web API method to use
     * @return Data is returned as a String in the given format (which may be
     *        "json", "vdf" or "xml").
     * @throws WebApiException In case of any request failure
     */
    public static String load(String format, String apiInterface, String method)
            throws WebApiException {
        return load(format, apiInterface, method, 1, null);
    }

    /**
     * Fetches data from Steam Web API using the specified interface, method and
     * version. Additional parameters are supplied via HTTP GET.
     * Data returned has the given format (which may be 'json', 'vdf', 'xml').
     *
     * @param apiInterface The Web API interface to use
     * @param format The format to use for the response. May be "json", "vdf"
     *               or "xml".
     * @param method The Web API method to use
     * @param version The interface version to use
     * @return Data is returned as a String in the given format (which may be
     *        "json", "vdf" or "xml").
     * @throws WebApiException In case of any request failure
     */
    public static String load(String format, String apiInterface, String method, int version)
            throws WebApiException {
        return load(format, apiInterface, method, version, null);
    }

    /**
     * Fetches data from Steam Web API using the specified interface, method and
     * version. Additional parameters are supplied via HTTP GET.
     * Data returned has the given format (which may be 'json', 'vdf', 'xml').
     *
     * @param apiInterface The Web API interface to use
     * @param format The format to use for the response. May be "json", "vdf"
     *               or "xml".
     * @param method The Web API method to use
     * @param params The HTTP GET parameters to send with the request
     * @param version The interface version to use
     * @return Data is returned as a String in the given format (which may be
     *        "json", "vdf", or "xml").
     * @throws WebApiException In case of any request failure
     */
    public static String load(String format, String apiInterface, String method, int version, Map<String, Object> params)
            throws WebApiException {
        String url = String.format("http://api.steampowered.com/%s/%s/v%04d/?", apiInterface, method, version);

        if(params == null) {
            params = new HashMap<String, Object>();
        }
        params.put("format", format);
        params.put("key", apiKey);

        boolean first = true;
        for(Map.Entry<String, Object> param : params.entrySet()) {
            if(first) {
                first = false;
            } else {
                url += '&';
            }

            url += String.format("%s=%s", param.getKey(), param.getValue());
        }

        String data;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_AUTHENTICATION, false);
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            Integer statusCode = response.getStatusLine().getStatusCode();
            if(!statusCode.toString().startsWith("20")) {
                if(statusCode == 401) {
                    throw new WebApiException(WebApiException.Cause.UNAUTHORIZED);
                }

                throw new WebApiException(WebApiException.Cause.HTTP_ERROR, statusCode, response.getStatusLine().getReasonPhrase());
            }

            data = EntityUtils.toString(response.getEntity());
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

}
