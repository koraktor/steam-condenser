/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

public class TF2Sniper extends TF2Class
{
    private int maxHeadshots;

    public TF2Sniper(Element classData)
    {
	super(classData);

	this.maxHeadshots = Integer.parseInt(classData.getElementsByTagName("iheadshots").item(0).getTextContent());
    }

    public int getMaxHeadshots()
    {
	return this.maxHeadshots;
    }
}
