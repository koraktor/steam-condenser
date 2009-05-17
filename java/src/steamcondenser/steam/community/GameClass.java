/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package steamcondenser.steam.community;

/**
 * Super class for classes representing player classes
 * @author Sebastian Staudt
 */
abstract public class GameClass {

	protected String name;

	protected int playTime;

	public String getName() {
		return this.name;
	}

	public int getPlayTime() {
		return this.playTime;
	}

}
