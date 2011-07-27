/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

/**
 * An abstract class implementing basic functionality for classes representing
 * player classes
 *
 * @author Sebastian Staudt
 */
public abstract class GameClass {

    protected String name;

    protected int playTime;

    /**
     * Returns the name of this class
     *
     * @return [String] The name of this class
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the time in minutes the player has played with this class
     *
     * @return The time this class has been played
     */
    public int getPlayTime() {
        return this.playTime;
    }

}
