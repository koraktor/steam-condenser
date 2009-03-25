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

    public GameAchievement(int appId, String name, boolean done)
    {
        this.appId = appId;
        this.done = done;
        this.name = name;
    }

    public int getAppId()
    {
        return this.appId;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isDone()
    {
        return this.done;
    }
}
