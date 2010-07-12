<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Exceptions
 */

require_once STEAM_CONDENSER_PATH . 'exceptions/SteamCondenserException.php';

/**
 * This exceptions is raised when a Steam Web API request or a related action
 * fails. This can have various reasons.
 *
 * @package    Steam Condenser (PHP)
 * @subpackage Exceptions
 */
class WebApiException extends SteamCondenserException {

    const HTTP_ERROR   = 0;

    const INVALID_KEY  = 1;

    const STATUS_BAD   = 2;

    const UNAUTHORIZED = 3;

    /**
     * Creates a new WebApiException with an error message according to the
     * given $cause. If this cause is STATUS_BAD (which will origin from the
     * Web API itself) or HTTP_ERROR the details about this failed request will
     * be taken from $statusCode and $statusMessage+.
     *
     * * HTTP_ERROR:   An error in the HTTP request itself will result in an
     *                 exception with this reason.
     * * INVALID_KEY:  This occurs when trying to set a Web API key that isn't
     *                 valid, i.e. a 128 bit integer in a hexadecimal string.
     * * STATUS_BAD:   This is caused by a succesful request that fails for
     *                 some Web API internal reason (e.g. a invalid argument).
     *                 Details about this failed request will be taken from
     *                 $statusCode and $statusMessage.
     * * UNAUTHORIZED: This happens when a Steam Web API request is rejected as
     *                 unauthorized. This most likely means that you did not
     *                 specify a valid Web API key using WebAPI::setApiKey(). A
     *                 Web API key can be obtained from
     *                 http://steamcommunity.com/dev/apikey.
     *
     * Other undefined reasons will cause a generic error message.
     */
    public function __construct($cause, $statusCode = null, $statusMessage = '') {
        switch($cause) {
            case self::HTTP_ERROR:
                $message = "The Web API request has failed due to an HTTP error: $statusMessage (status code: $statusCode).";
                break;
            case self::INVALID_KEY:
                $message = 'This is not a valid Steam Web API key.';
                break;
            case self::STATUS_BAD:
                $message = "The Web API request failed with the following error: $statusMessage (status code: $statusCode).";
                break;
            case self::UNAUTHORIZED:
                $message = 'Your Web API request has been rejected. You most likely did not specify a valid Web API key.';
                break;
            default:
                $message = 'An unexpected error occured while executing a Web API request.';
        }

        parent::__construct($message);
    }

}
?>
