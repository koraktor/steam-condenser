/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

public class TF2Spy extends TF2Class
{
    private int maxBackstabs;

    private int maxHealthLeeched;

    public TF2Spy(Element classData)
    {
	super(classData);

	this.maxBackstabs      = Integer.parseInt(classData.getElementsByTagName("ibackstabs").item(0).getTextContent());
	this.maxHealthLeeched  = Integer.parseInt(classData.getElementsByTagName("ihealthpointsleached").item(0).getTextContent());
    }

    public int getMaxBackstabs()
    {
	return this.maxBackstabs;
    }

    public int getMaxHealthLeeched()
    {
	return this.maxHealthLeeched;
    }
}
