/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */
package com.github.koraktor.steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

/**
 * The <code>TF2ClassFactory</code> is used to created instances of
 * <code>TF2Class</code> based on the XML input data
 *
 * @author Sebastian Staudt
 */
abstract class TF2ClassFactory
{
    /**
     * Creates a new instance of a TF2 class instance based on the given XML
     * data
     *
     * This returns an instance of <code>TF2Class</code> or its subclasses
     * <code>TF2Engineer</code>, <code>TF2Medic</code>, <code>TF2Sniper</code>
     * or <code>TF2Spy</code> depending on the given XML data.
     *
     * @param classData The XML data for the class
     * @return The statistics for the given class data
     */
    public static TF2Class getTF2Class(Element classData) {
        String className = classData.getElementsByTagName("className").item(0).getTextContent();

        if(className.equals("Engineer")) {
            return new TF2Engineer(classData);
        } else if(className.equals("Medic")) {
            return new TF2Medic(classData);
        } else if(className.equals("Sniper")) {
            return new TF2Sniper(classData);
        } else if(className.equals("Spy")) {
            return new TF2Spy(classData);
        } else {
            return new TF2Class(classData);
        }
    }
}
