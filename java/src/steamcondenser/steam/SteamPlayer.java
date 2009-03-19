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
     * Creates a new SteamPlayer object with the given information
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
     * @return Returns the time this player is connected to the server
     */
    public float getConnectTime()
    {
        return this.connectTime;
    }

    /**
     * @return Returns the ID of this player
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * @return Returns the nickname of this player
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return Returns the score of this player
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * @return A String representation of this player
     */
    @Override
    public String toString()
    {
	return "#" + this.id + " \"" + this.name + "\", Score: " + this.score + ", Time: " + this.connectTime;
    }
}
