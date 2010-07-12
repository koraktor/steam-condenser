/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.exceptions;

/**
 * This adds support for Steam Web API to classes needing this functionality.
 * The Web API requires you to register a domain with your Steam account to
 * acquire an API key. See http://steamcommunity.com/dev for further details.
 *
 * @author Sebastian Staudt
 */
public class WebApiException extends SteamCondenserException {

    public enum Cause { HTTP_ERROR, INVALID_KEY, STATUS_BAD, UNAUTHORIZED }

    private String message;

    /**
     * Creates a new <code>WebApiException</code> with an error message
     * according to the given <code>cause</code>.
     *
     * @param cause Specifies the cause for this exception which may be one of
     *              the following:
     * <li><code>HTTP_ERROR</code>:   An error in the HTTP request itself will
     *                                result in an exception with this reason.
     * <li><code>INVALID_KEY</code>:  This occurs when trying to set a Web API
     *                                key that isn't valid, i.e. a 128 bit
     *                                integer in a hexadecimal string.
     * <li><code>STATUS_BAD</code>:   This is caused by a succesful request
     *                                that fails for some Web API internal
     *                                reason (e.g. a invalid argument). Details
     *                                about this failed request will be taken
     *                                from <code>statusCode</code> and
     *                                <code>statusMessage</code>.
     * <li><code>UNAUTHORIZED</code>: This happens when a Steam Web API request
     *                                is rejected as unauthorized. This most
     *                                likely means that you did not specify a
     *                                valid Web API key using
     *                                <code>WebAPI.setApiKey()</code>. A Web
     *                                API key can be obtained from
     *                                http://steamcommunity.com/dev/apikey.
     *
     * Other undefined reasons will cause a generic error message.
     */
    public WebApiException(Cause cause) {
        this(cause, null, null);
    }

    /**
     * Creates a new <code>WebApiException</code> with an error message
     * according to the given <code>cause</code>. If this cause is
     * <code>STATUS_BAD</code> (which will origin from the Web API itself) or
     * <code>HTTP_ERROR</code> the details about this failed request will be
     * taken from <code>statusCode</code> and <code>statusMessage</code>.
     *
     * @param cause Specifies the cause for this exception which may be one of
     *              the following:
     * <li><code>HTTP_ERROR</code>:   An error in the HTTP request itself will
     *                                result in an exception with this reason.
     * <li><code>INVALID_KEY</code>:  This occurs when trying to set a Web API
     *                                key that isn't valid, i.e. a 128 bit
     *                                integer in a hexadecimal string.
     * <li><code>STATUS_BAD</code>:   This is caused by a succesful request
     *                                that fails for some Web API internal
     *                                reason (e.g. a invalid argument). Details
     *                                about this failed request will be taken
     *                                from <code>statusCode</code> and
     *                                <code>statusMessage</code>.
     * <li><code>UNAUTHORIZED</code>: This happens when a Steam Web API request
     *                                is rejected as unauthorized. This most
     *                                likely means that you did not specify a
     *                                valid Web API key using
     *                                <code>WebAPI.setApiKey()</code>. A Web
     *                                API key can be obtained from
     *                                http://steamcommunity.com/dev/apikey.
     *
     * Other undefined reasons will cause a generic error message.
     *
     * @param statusCode    Will specify the status code for errors of type
     *                      HTTP_ERROR and STATUS_BAD
     * @param statusMessage Will specify a status message for errors of type
     *                      HTTP_ERROR and STATUS_BAD
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
