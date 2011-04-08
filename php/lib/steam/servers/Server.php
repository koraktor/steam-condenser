<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Servers
 */

/**
 * This class is subclassed by all classes implementing server functionality
 *
 * It provides basic name resolution features.
 *
 * @author Sebastian Staudt
 */
abstract class Server {

    /**
     * @var array
     */
    protected $hostNames;

    /**
     * @var array
     */
    protected $ipAddresses;

    /**
     * @var int
     */
    protected $ipIndex;

    /**
     * @var int
     */
    protected $port;

    /**
     * Creates a new server instance with the given address and port
     *
     * @param string $address
     * @param int $port
     */
    public function __construct($address, $port = null) {
        $address = strval($address);

        if(strpos($address, ':') !== false) {
            $address = explode(':', $address, 2);
            $port    = $address[1];
            $address = $address[0];
        }
        $this->hostNames   = array();
        $this->ipAddresses = array();
        $this->ipIndex     = 0;
        $this->port        = intval($port);

        $addresses = gethostbynamel($address);
        if(empty($addresses)) {
            throw new SteamCondenserException("Cannot resolve $address");
        }

        foreach($addresses as $address) {
            $this->hostNames[]   = gethostbyaddr($address);
            $this->ipAddresses[] = $address;
        }

        $this->ipAddress = $this->ipAddresses[0];


        $this->initSocket();
    }

    /**
     * Rotate this server's IP address to the next one in the IP list
     *
     * This method will return <code>true</code>, if the IP list reached its
     * end. If the list contains only one IP address, this method will
     * instantly return <code>true</code>.
     *
     * @return bool
     */
    public function rotateIp() {
        if(sizeof($this->ipAddresses) == 1) {
            return true;
        }

        $this->ipIndex   = ($this->ipIndex + 1) % sizeof($this->ipAddresses);
        $this->ipAddress = $this->ipAddresses[$this->ipIndex];

        $this->initSocket();

        return $this->ipIndex == 0;
    }

    /**
     * Initializes the socket(s) to communicate with the server
     */
    protected abstract function initSocket();

}
