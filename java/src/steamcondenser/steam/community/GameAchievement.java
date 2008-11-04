/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.community;

/**
 * The GameAchievement class represents a specific achievement for a single game
 * and for a single user
 * @author Sebastian Staudt
 * @version $Id$
 */
public class GameAchievement
{
    private int appId;

    private boolean done;

    private String name;

    private int steamId;

    public GameAchievement(int steamId, int appId, String name, boolean done)
    {
	this.appId   = appId;
	this.done    = done;
	this.name    = name;
	this.steamId = steamId;
    }

    public int getAppId()
    {
	return this.appId;
    }

    public String getName()
    {
	return this.name;
    }

    public int getSteamId()
    {
	return this.steamId;
    }

    public boolean isDone()
    {
	return this.done;
    }
}
