/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.exceptions;

/**
 * This exception is raised when a Steam Web API request or a related action
 * fails. This can have codeious reasons like an invalid Web API key or a broken
 * request.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.community.WebApi
 */
public class WebApiException extends SteamCondenserException {

    public enum Cause { HTTP_ERROR, INVALID_KEY, STATUS_BAD, UNAUTHORIZED }

    private String message;

    /**
     * Creates a new WebApiException with an error message according to the
     * given <code>$cause</code>. If this cause is <code>STATUS_BAD</code> (which
     * will origin from the Web API itself) or <code>HTTP_ERROR</code> the
     * details about this failed request will be taken from
     * <code>$statusCode</code> and <code>$statusMessage</code>.
     *
     * @param cause An integer indicating the problem which caused this
     *        exception:
     *
     *        <ul>
     *        <li><code>HTTP_ERROR</code>: An error during the HTTP request
     *            itself will result in an exception with this reason.</li>
     *        <li><code>INVALID_KEY</code>: This occurs when trying to set a
     *            Web API key that isn't valid, i.e. a 128 bit integer in a
     *            hexadecimal string.
     *        <li><code>STATUS_BAD</code>: This is caused by a successful
     *            request that fails for some Web API internal reason (e.g. an
     *            invalid argument). Details about this failed request will be
     *            taken from <code>statusCode</code> and
     *            <code>statusMessage</code>.
     *        <li><code>UNAUTHORIZED</code>: This happens when a Steam Web API
     *            request is rejected as unauthorized. This most likely means
     *            that you did not specify a valid Web API key using
     *            {@link com.github.koraktor.steamcondenser.steam.community.WebApi#setApiKey}.
     *            A Web API key can be obtained from
     *            http://steamcommunity.com/dev/apikey.
     *        </ul>
     *
     *        Other undefined reasons will cause a generic error message.
     */
    public WebApiException(Cause cause) {
        this(cause, null, null);
    }

    /**
     * Creates a new WebApiException with an error message according to the
     * given <code>cause</code>. If this cause is <code>STATUS_BAD</code>
     * (which will origin from the Web API itself) or <code>HTTP_ERROR</code>
     * the details about this failed request will be taken from
     * <code>statusCode</code> and <code>statusMessage</code>.
     *
     * @param cause An integer indicating the problem which caused this
     *        exception:
     *
     *        <ul>
     *        <li><code>HTTP_ERROR</code>: An error during the HTTP request
     *            itself will result in an exception with this reason.</li>
     *        <li><code>INVALID_KEY</code>: This occurs when trying to set a
     *            Web API key that isn't valid, i.e. a 128 bit integer in a
     *            hexadecimal string.
     *        <li><code>STATUS_BAD</code>: This is caused by a successful
     *            request that fails for some Web API internal reason (e.g. an
     *            invalid argument). Details about this failed request will be
     *            taken from <code>statusCode</code> and
     *            <code>statusMessage</code>.
     *        <li><code>UNAUTHORIZED</code>: This happens when a Steam Web API
     *            request is rejected as unauthorized. This most likely means
     *            that you did not specify a valid Web API key using
     *            {@link com.github.koraktor.steamcondenser.steam.community.WebApi#setApiKey}.
     *            A Web API key can be obtained from
     *            http://steamcommunity.com/dev/apikey.
     *        </ul>
     *
     *        Other undefined reasons will cause a generic error message.
     * @param statusCode The HTTP status code returned by the Web API
     * @param statusMessage The status message returned in the response
     */
    public WebApiException(Cause cause, Integer statusCode, String statusMessage) {
        switch(cause) {
            case HTTP_ERROR:
                this.message = "The Web API request has failed due to an HTTP error: " + statusMessage + " (status code: " + statusCode + ").";
                break;
            case INVALID_KEY:
                this.message = "This is not a valid Steam Web API key.";
                break;
            case STATUS_BAD:
                this.message = "The Web API request failed with the following error: " + statusMessage + " (status code: " + statusCode + ").";
                break;
            case UNAUTHORIZED:
                this.message = "Your Web API request has been rejected. You most likely did not specify a valid Web API key.";
                break;
            default:
                this.message = "An unexpected error occured while executing a Web API request.";
        }
    }

    public String getMessage() {
        return this.message;
    }
}
