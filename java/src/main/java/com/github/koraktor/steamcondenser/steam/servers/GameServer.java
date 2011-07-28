/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.servers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.github.koraktor.steamcondenser.steam.packets.A2S_INFO_Packet;
import com.github.koraktor.steamcondenser.steam.packets.S2A_INFO_BasePacket;
import com.github.koraktor.steamcondenser.steam.packets.A2S_PLAYER_Packet;
import com.github.koraktor.steamcondenser.steam.packets.S2A_PLAYER_Packet;
import com.github.koraktor.steamcondenser.steam.packets.A2S_RULES_Packet;
import com.github.koraktor.steamcondenser.steam.packets.S2A_RULES_Packet;
import com.github.koraktor.steamcondenser.steam.packets.A2S_SERVERQUERY_GETCHALLENGE_Packet;
import com.github.koraktor.steamcondenser.steam.packets.S2C_CHALLENGE_Packet;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;
import com.github.koraktor.steamcondenser.steam.sockets.QuerySocket;

/**
 * This class is subclassed by classes representing different game server
 * implementations and provides the basic functionality to communicate with
 * them using the common query protocol
 *
 * @author Sebastian Staudt
 */
public abstract class GameServer extends Server {

    private static final int REQUEST_CHALLENGE = 0;
    private static final int REQUEST_INFO = 1;
    private static final int REQUEST_PLAYER = 2;
    private static final int REQUEST_RULES = 3;
    protected int challengeNumber = 0xFFFFFFFF;
    protected int ping;
    protected HashMap<String, SteamPlayer> playerHash;
    protected boolean rconAuthenticated;
    protected int rconRequestId;
    protected HashMap<String, String> rulesHash;
    protected HashMap<String, Object> serverInfo;
    protected QuerySocket socket;

    /**
     * Creates a new instance of a game server object
     *
     * @param address Either an IP address, a DNS name or one of them combined
     *        with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @param port The port the server is listening on
     * @throws IOException if initializing the socket fails
     * @throws SteamCondenserException if an host name cannot be resolved
     */
    protected GameServer(String address, Integer port)
            throws IOException, SteamCondenserException {
        super(address, port);

        this.rconAuthenticated = false;
    }

    /**
     * Parses the player attribute names supplied by <code>rcon status</code>
     *
     * @param statusHeader The header line provided by <code>rcon status</code>
     * @return array Split player attribute names
     * @see #splitPlayerStatus
     */
    private static List<String> getPlayerStatusAttributes(String statusHeader) {
        List<String> statusAttributes = new ArrayList<String>();
        for(String attribute : statusHeader.split("\\s+")) {
            if(attribute.equals("connected")) {
                statusAttributes.add("time");
            } else if(attribute.equals("frag")) {
                statusAttributes.add("score");
            } else {
                statusAttributes.add(attribute);
            }
        }

        return statusAttributes;
    }

    /**
     * Splits the player status obtained with <code>rcon status</code>
     *
     * @param attributes The attribute names
     * @param playerStatus The status line of a single player
     * @return array The attributes with the corresponding values for this
     *         player
     * @see #getPlayerStatusAttributes
     */
    private static Map<String, String> splitPlayerStatus(List<String> attributes, String playerStatus) {
        if(!attributes.get(0).equals("userid")) {
            playerStatus = playerStatus.replaceAll("^\\d+ +", "");
        }

        int firstQuote = playerStatus.indexOf('"');
        int lastQuote  = playerStatus.lastIndexOf('"');
        List<String> tmpData = new ArrayList<String>();
        tmpData.add(playerStatus.substring(0, firstQuote));
        tmpData.add(playerStatus.substring(firstQuote + 1, lastQuote));
        tmpData.add(playerStatus.substring(lastQuote + 1));

        List<String> data = new ArrayList<String>();
        data.addAll(Arrays.asList(tmpData.get(0).trim().split("\\s+")));
        data.add(tmpData.get(1));
        data.addAll(Arrays.asList(tmpData.get(2).trim().split("\\s+")));
        data.remove("");

        if(attributes.size() > data.size() && attributes.contains("state")) {
            data.add(3, null);
            data.add(3, null);
            data.add(3, null);
        } else if(attributes.size() < data.size()) {
            data.remove(1);
        }

        Map<String, String> playerData = new HashMap<String, String>();
        for(int i = 0; i < data.size(); i ++) {
            playerData.put(attributes.get(i), data.get(i));
        }

        return playerData;
    }

    /**
     * Returns the last measured response time of this server
     * <p/>
     * If the latency hasn't been measured yet, it is done when calling this
     * method for the first time.
     * <p/>
     * If this information is vital to you, be sure to call
     * {@link #updatePing} regularly to stay up-to-date.
     *
     * @return The latency of this server in milliseconds
     * @see #updatePing
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public int getPing()
            throws IOException, TimeoutException, SteamCondenserException {
        if(this.ping == 0) {
            this.updatePing();
        }

        return this.ping;
    }

    /**
     * Returns a list of players currently playing on this server
     * <p/>
     * If the players haven't been fetched yet, it is done when calling this
     * method for the first time.
     * <p/>
     * As the players and their scores change quite often be sure to update
     * this list regularly by calling {@link #updatePlayers} if you rely on
     * this information.
     *
     * @return The players on this server
     * @see #updatePlayers
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public HashMap<String, SteamPlayer> getPlayers()
            throws IOException, SteamCondenserException, TimeoutException {
        return this.getPlayers(null);
    }

    /**
     * Returns a list of players currently playing on this server
     * <p/>
     * If the players haven't been fetched yet, it is done when calling this
     * method for the first time.
     * <p/>
     * As the players and their scores change quite often be sure to update
     * this list regularly by calling {@link #updatePlayers} if you rely on
     * this information.
     *
     * @param rconPassword The RCON password of this server may be provided to
     *        gather more detailed information on the players, like STEAM_IDs.
     * @return The players on this server
     * @see #updatePlayers
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public HashMap<String, SteamPlayer> getPlayers(String rconPassword)
            throws IOException, SteamCondenserException, TimeoutException {
        if(this.playerHash == null) {
            this.updatePlayers(rconPassword);
        }

        return this.playerHash;
    }

    /**
     * Returns the settings applied on the server. These settings are also
     * called rules.
     * <p/>
     * If the rules haven't been fetched yet, it is done when calling this
     * method for the first time.
     * <p/>
     * As the rules usually don't change often, there's almost no need to
     * update this hash. But if you need to, you can achieve this by calling
     * {@link #updateRules}.
     *
     * @return The currently active server rules
     * @see #updateRules
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public HashMap<String, String> getRules()
            throws IOException, SteamCondenserException, TimeoutException {
        if(this.rulesHash == null) {
            this.updateRules();
        }

        return this.rulesHash;
    }

    /**
     * Returns an associative array with basic information on the server.
     * <p/>
     * If the server information haven't been fetched yet, it is done when
     * calling this method for the first time.
     * <p/>
     * The server information usually only changes on map change and when
     * players join or leave. As the latter changes can be monitored by calling
     * {@link #updatePlayers}, there's no need to call
     * {@link #updateServerInfo} very often.
     *
     * @return Server attributes with their values
     * @see #updateServerInfo
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public HashMap<String, Object> getServerInfo()
            throws IOException, SteamCondenserException, TimeoutException {
        if(this.serverInfo == null) {
            this.updateServerInfo();
        }

        return this.serverInfo;
    }

    /**
     * Receives a response from the server
     *
     * @return The response packet replied by the server
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    private SteamPacket getReply()
            throws IOException, TimeoutException, SteamCondenserException {
        return this.socket.getReply();
    }

    /**
     * Sends the specified request to the server and handles the returned
     * response
     * <p/>
     * Depending on the given request type this will fill the various data
     * attributes of the server object.
     *
     * @param requestType The type of request to send to the server
     * @throws IOException if the request fails
     * @throws SteamCondenserException if either the request type or the
     *         response packet is not known or a problem occurs while parsing
     *         the reply
     * @throws TimeoutException if the request times out
     */
    private void handleResponseForRequest(int requestType)
            throws IOException, TimeoutException, SteamCondenserException {
        this.handleResponseForRequest(requestType, true);
    }

    /**
     * Sends the specified request to the server and handles the returned
     * response
     * <p/>
     * Depending on the given request type this will fill the various data
     * attributes of the server object.
     *
     * @param requestType The type of request to send to the server
     * @param repeatOnFailure Whether the request should be repeated, if
     *        the replied packet isn't expected. This is useful to handle
     *        missing challenge numbers, which will be automatically filled in,
     *        although not requested explicitly.
     * @throws IOException if the request fails
     * @throws SteamCondenserException if either the request type or the
     *         response packet is not known or a problem occurs while parsing
     *         the reply
     * @throws TimeoutException if the request times out
     */
    private void handleResponseForRequest(int requestType, boolean repeatOnFailure)
            throws IOException, TimeoutException, SteamCondenserException {
        Class<? extends SteamPacket> expectedResponse = null;
        SteamPacket requestPacket = null;

        try {
            switch(requestType) {
                case GameServer.REQUEST_CHALLENGE:
                    expectedResponse = S2C_CHALLENGE_Packet.class;
                    requestPacket = new A2S_SERVERQUERY_GETCHALLENGE_Packet();
                    break;
                case GameServer.REQUEST_INFO:
                    expectedResponse = S2A_INFO_BasePacket.class;
                    requestPacket = new A2S_INFO_Packet();
                    break;
                case GameServer.REQUEST_PLAYER:
                    expectedResponse = S2A_PLAYER_Packet.class;
                    requestPacket = new A2S_PLAYER_Packet(this.challengeNumber);
                    break;
                case GameServer.REQUEST_RULES:
                    expectedResponse = S2A_RULES_Packet.class;
                    requestPacket = new A2S_RULES_Packet(this.challengeNumber);
                    break;
                default:
                    throw new SteamCondenserException("Called with wrong request type.");
            }

            this.sendRequest(requestPacket);

            SteamPacket responsePacket = this.getReply();

            if(responsePacket.getClass().getSuperclass().equals(S2A_INFO_BasePacket.class)) {
                this.serverInfo = ((S2A_INFO_BasePacket) responsePacket).getInfoHash();
            } else if(responsePacket instanceof S2A_PLAYER_Packet) {
                this.playerHash = ((S2A_PLAYER_Packet) responsePacket).getPlayerHash();
            } else if(responsePacket instanceof S2A_RULES_Packet) {
                this.rulesHash = ((S2A_RULES_Packet) responsePacket).getRulesHash();
            } else if(responsePacket instanceof S2C_CHALLENGE_Packet) {
                this.challengeNumber = ((S2C_CHALLENGE_Packet) responsePacket).getChallengeNumber();
            } else {
                throw new SteamCondenserException("Response of type " + responsePacket.getClass() + " cannot be handled by this method.");
            }

            if(responsePacket.getClass() != expectedResponse) {
                System.out.println("Expected " + expectedResponse + ", got " + responsePacket.getClass() + ".");
                if(repeatOnFailure) {
                    this.handleResponseForRequest(requestType, false);
                }
            }
        } catch(TimeoutException e) {
            System.out.println("Expected " + expectedResponse + ", but timed out. The server is probably offline.");
        }
    }

    /**
     * Initializes this server object with basic information
     *
     * @see #updateChallengeNumber
     * @see #updatePing
     * @see #updateServerInfo
     * @throws IOException if a request fails
     * @throws SteamCondenserException if a problem occurs while parsing a
     *         reply
     * @throws TimeoutException if a request times out
     */
    public void initialize()
            throws IOException, TimeoutException, SteamCondenserException {
        this.updatePing();
        this.updateServerInfo();
        this.updateChallengeNumber();
    }

    /**
     * Returns whether the RCON connection to this server is already
     * authenticated
     *
     * @return <code>true</code> if the RCON connection is authenticated
     * @see #rconAuth
     */
    public boolean isRconAuthenticated() {
        return this.rconAuthenticated;
    }

    /**
     * Authenticates with the server for RCON communication
     *
     * @param password The RCON password of the server
     * @return <code>true</code>, if the authentication was successful
     * @see #rconExec
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    abstract public boolean rconAuth(String password)
            throws IOException, TimeoutException, SteamCondenserException;

    /**
     * Remotely executes a command on the server via RCON
     *
     * @param command The command to execute on the server via RCON
     * @return The output of the executed command
     * @see #rconAuth
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    abstract public String rconExec(String command)
            throws IOException, TimeoutException, SteamCondenserException;

    /**
     * Sends a request packet to the server
     *
     * @param requestData The request packet to send to the server
     * @throws IOException if the request fails
     */
    private void sendRequest(SteamPacket requestData)
            throws IOException {
        this.socket.send(requestData);
    }

    /**
     * Returns a human-readable text representation of the server
     *
     * @return string Available information about the server in a
     *         human-readable format
     */
    @Override
    public String toString() {
        String returnString = "";

        returnString += "Ping: " + this.ping + "\n";
        returnString += "Challenge number: " + this.challengeNumber + "\n";

        if(this.serverInfo != null) {
            returnString += "Info:" + "\n";
            for(Entry<String, Object> info : this.serverInfo.entrySet()) {
                returnString += "  " + info.getKey() + ": " + info.getValue() + "\n";
            }
        }

        if(this.playerHash != null) {
            returnString += "Players:" + "\n";
            for(SteamPlayer player : this.playerHash.values()) {
                returnString += "  " + player + "\n";
            }
        }

        if(this.rulesHash != null) {
            returnString += "Rules:" + "\n";
            for(Entry<String, String> rule : this.rulesHash.entrySet()) {
                returnString += "  " + rule.getKey() + ": " + rule.getValue() + "\n";
            }
        }

        return returnString;
    }

    /**
     * Sends a A2S_SERVERQUERY_GETCHALLENGE request to the server and updates
     * the challenge number used to communicate with this server
     * <p/>
     * There's usually no need to call this method explicitly, because
     * {@link #handleResponseForRequest} will automatically get the challenge
     * number when the server assigns a new one.
     *
     * @see #handleResponseForRequest
     * @see #initialize
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public void updateChallengeNumber()
            throws IOException, TimeoutException, SteamCondenserException {
        this.handleResponseForRequest(GameServer.REQUEST_CHALLENGE);
    }

    /**
     * Sends a A2S_INFO request to the server and measures the time needed for
     * the reply
     * <p/>
     * If this information is vital to you, be sure to call this method
     * regularly to stay up-to-date.
     *
     * @see #getPing
     * @see #initialize
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public void updatePing()
            throws IOException, TimeoutException, SteamCondenserException {
        this.sendRequest(new A2S_INFO_Packet());
        long startTime = System.currentTimeMillis();
        this.getReply();
        long endTime = System.currentTimeMillis();
        this.ping = Long.valueOf(endTime - startTime).intValue();
    }

    /**
     * Sends a A2S_PLAYERS request to the server and updates the players' data
     * for this server
     * <p/>
     * As the players and their scores change quite often be sure to update
     * this list regularly by calling this method if you rely on this
     * information.
     *
     * @see #getPlayers
     * @see #handleResponseForRequest
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public void updatePlayers()
            throws IOException, TimeoutException, SteamCondenserException {
        this.updatePlayers(null);
    }

    /**
     * Sends a A2S_PLAYERS request to the server and updates the players' data
     * for this server
     * <p/>
     * As the players and their scores change quite often be sure to update
     * this list regularly by calling this method if you rely on this
     * information.
     *
     * @param rconPassword The RCON password of this server may be provided to
     *        gather more detailed information on the players, like STEAM_IDs.
     * @see #getPlayers
     * @see #handleResponseForRequest
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public void updatePlayers(String rconPassword)
            throws IOException, TimeoutException, SteamCondenserException {
        this.handleResponseForRequest(GameServer.REQUEST_PLAYER);

        if(!this.rconAuthenticated) {
            if(rconPassword == null) {
                return;
            }
            this.rconAuth(rconPassword);
        }

        List<String> players = new ArrayList<String>();
        for(String line : Arrays.asList(this.rconExec("status").split("\n"))) {
            if(line.startsWith("#") && !line.equals("#end")) {
                players.add(line.substring(1).trim());
            }
        }
        List<String> attributes = getPlayerStatusAttributes(players.remove(0));

        for(String player : players) {
            Map<String, String> playerData = splitPlayerStatus(attributes, player);
            String playerName = playerData.get("name");
            if(this.playerHash.containsKey(playerName)) {
                this.playerHash.get(playerName).addInformation(playerData);
            }
        }
    }

    /**
     * Sends a A2S_RULES request to the server and updates the rules of this
     * server
     * <p/>
     * As the rules usually don't change often, there's almost no need to
     * update this hash. But if you need to, you can achieve this by calling
     * this method.
     *
     * @see #getRules
     * @see #handleResponseForRequest
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public void updateRules()
            throws IOException, TimeoutException, SteamCondenserException {
        this.handleResponseForRequest(GameServer.REQUEST_RULES);
    }

    /**
     * Sends a A2S_INFO request to the server and updates this server's basic
     * information
     * <p/>
     * The server information usually only changes on map change and when
     * players join or leave. As the latter changes can be monitored by calling
     * {@link #updatePlayers}, there's no need to call this method very often.
     *
     * @see #getServerInfo
     * @see #handleResponseForRequest
     * @see #initialize
     * @throws IOException if the request fails
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public void updateServerInfo()
            throws IOException, TimeoutException, SteamCondenserException {
        this.handleResponseForRequest(GameServer.REQUEST_INFO);
    }
}
