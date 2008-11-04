/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam;

/**
 * A player on a GameServer
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SteamPlayer
{
    private float connectTime;

    private int id;

    private String name;

    private int score;

    /**
     * Creates a new player with the given parameters
     * @param id The ID of the player
     * @param name The nickname of the player
     * @param score The score of the player
     * @param connectTime The time the player is connected to the server
     */
    public SteamPlayer(int id, String name, int score, float connectTime)
    {
	this.connectTime = connectTime;
	this.id = id;
	this.name = name;
	this.score = score;
    }

    /**
     * @return A String representation of this player.
     */
    public String toString()
    {
	return "#" + this.id + " \"" + this.name + "\" " + this.score + " " + this.connectTime;
    }
}
