/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import com.github.koraktor.steamcondenser.steam.community.WebApi;

/**
 * Represents the special Team Fortress 2 item Golden Wrench. It includes the
 * ID of the item, the serial number of the wrench, a reference to the SteamId
 * of the owner and the date this player crafted the wrench
 *
 * @author Sebastian Staudt
 */
public class TF2GoldenWrench {

    private static Set<TF2GoldenWrench> goldenWrenches = null;

    private Date date;

    private int id;

    private int number;

    private SteamId owner;

    /**
     * Returns a Set of all golden wrenches (as instances of TF2GoldenWrench)
     *
     * @return A Set containing all golden wrenches
     * @throws JSONException On JSON errors
     * @throws SteamCondenserException If an error occurs querying the Web API
     *                                 or the Steam Community
     */
    public static Set<TF2GoldenWrench> getGoldenWrenches()
            throws JSONException, SteamCondenserException {
        if(goldenWrenches == null) {
            goldenWrenches = new HashSet<TF2GoldenWrench>();

            JSONObject data = new JSONObject(WebApi.getJSON("ITFItems_440", "GetGoldenWrenches"));
            JSONArray wrenches = data.getJSONObject("results").getJSONObject("wrenches").getJSONArray("wrench");
            for(int i = 0; i < wrenches.length(); i ++) {
                goldenWrenches.add(new TF2GoldenWrench(wrenches.getJSONObject(i)));
            }
        }

        return goldenWrenches;
    }

    /**
     * Creates a new instance of TF2GoldenWrench with the given data
     *
     * @param wrenchData The JSON data for this wrench
     * @throws JSONException If some attribute is missing from the JSON data
     * @throws SteamCondenserException If the SteamId for the owner of the
     *                                 wrench cannot be created
     */
    private TF2GoldenWrench(JSONObject wrenchData)
            throws JSONException, SteamCondenserException {
        this.date   = new Date(wrenchData.getLong("timestamp"));
        this.id     = wrenchData.getInt("itemID");
        this.number = wrenchData.getInt("wrenchNumber");
        this.owner  = SteamId.create(wrenchData.getLong("steamID"), false);
    }

    /**
     * Returns the date this Golden Wrench has been crafted
     *
     * @return The crafting date of this wrench
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Returns the unique item ID of this Golden Wrench
     *
     * @return The ID of this wrench
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the serial number of this Golden Wrench
     *
     * @return The serial of this wrench
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Returns the SteamId of the owner of this Golden Wrench
     *
     * @return The owner of this wrench
     * @see SteamId
     */
    public SteamId getOwner() {
        return this.owner;
    }
}
