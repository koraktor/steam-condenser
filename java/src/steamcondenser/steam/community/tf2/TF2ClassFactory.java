/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */
package steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

public class TF2ClassFactory
{
    public static TF2Class getTF2Class(Element classData)
    {
	String className = classData.getElementsByTagName("className").item(0).getTextContent();

	if(className == "Engineer")
	{
	    return new TF2Engineer(classData);
	}
	else if(className == "Medic")
	{
	    return new TF2Medic(classData);
	}
	else if(className == "Sniper")
	{
	    return new TF2Sniper(classData);
	}
	else if(className == "Spy")
	{
	    return new TF2Spy(classData);
	}
	else
	{
	    return new TF2Class(classData);
	}
    }
}
