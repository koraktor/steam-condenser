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
	protected int rconRequestId;
	protected HashMap<String, String> rulesHash;
	protected HashMap<String, Object> serverInfo;
	protected QuerySocket socket;

    /**
     * Creates a new game server instance with the given address and port
     *
     * @param address
     * @param port
     */
    protected GameServer(String address, Integer port)
            throws IOException, SteamCondenserException {
        super(address, port);
    }

    /**
     * Parses the player attribute names supplied by +rcon status+
     *
     * @param statusHeader
     * @return Split player attribute names
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
     * Splits the player status obtained with "rcon status"
     *
     * @param attributes
     * @param playerStatus
     * @return Split player data
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
	 * @return The response time of this server in milliseconds
	 */
	public int getPing()
			throws IOException, TimeoutException, SteamCondenserException {
		if(this.ping == 0) {
			this.updatePing();
		}
		return this.ping;
	}

	/**
	 * @return An HashMap of SteamPlayers representing all players on this
	 *         server
	 */
	public HashMap<String, SteamPlayer> getPlayers()
			throws IOException, SteamCondenserException, TimeoutException {
		return this.getPlayers(null);
	}

	/**
	 * @return An HashMap of SteamPlayers extended with additional information
	 *         from "rcon status" representing all players on this server
	 */
	public HashMap<String, SteamPlayer> getPlayers(String rconPassword)
			throws IOException, SteamCondenserException, TimeoutException {
		if(this.playerHash == null) {
			this.updatePlayerInfo(rconPassword);
		}
		return this.playerHash;
	}

	/**
	 * @return A HashMap containing the rules of this server
	 */
	public HashMap<String, String> getRules()
			throws IOException, SteamCondenserException, TimeoutException {
		if(this.rulesHash == null) {
			this.updateRulesInfo();
		}
		return this.rulesHash;
	}

	/**
	 * @return A HashMap containing basic information about the server
	 */
	public HashMap<String, Object> getServerInfo()
			throws IOException, SteamCondenserException, TimeoutException {
		if(this.serverInfo == null) {
			this.updateServerInfo();
		}
		return this.serverInfo;
	}

	/**
	 * Returns a packet sent by the server in response to a query
	 * @return Packet recieved from the server
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws SteamCondenserException
	 */
	private SteamPacket getReply()
			throws IOException, TimeoutException, SteamCondenserException {
		return this.socket.getReply();
	}

	/**
	 * @throws IOException
	 * @throws SteamCondenserException
	 * @throws TimeoutException
	 */
	private void handleResponseForRequest(int requestType)
			throws IOException, TimeoutException, SteamCondenserException {
		this.handleResponseForRequest(requestType, true);
	}

	/**
	 * @throws IOException
	 * @throws SteamCondenserException
	 * @throws TimeoutException
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
	 * Initializes the server object with basic data (ping, server info and
	 * challenge number)
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws SteamCondenserException
	 */
	public void initialize()
			throws IOException, TimeoutException, SteamCondenserException {
		this.updatePing();
		this.updateServerInfo();
		this.updateChallengeNumber();
	}

	abstract public boolean rconAuth(String password)
			throws IOException, TimeoutException, SteamCondenserException;

	abstract public String rconExec(String command)
			throws IOException, TimeoutException, SteamCondenserException;

	/**
	 * Sends a query packet to the server
	 * @param requestData The query packet to send to the server
	 * @throws IOException
	 */
	private void sendRequest(SteamPacket requestData)
			throws IOException {
		this.socket.send(requestData);
	}

	/**
	 * Returns a String representation of this server
	 * @return A human readable version of this server's information
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
	 * Get the challenge number from the server
	 * @throws IOException
	 * @throws SteamCondenserException
	 * @throws TimeoutException
	 */
	public void updateChallengeNumber()
			throws IOException, TimeoutException, SteamCondenserException {
		this.handleResponseForRequest(GameServer.REQUEST_CHALLENGE);
	}

	/**
	 * Pings the server
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws SteamCondenserException
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
	 * Gets information about the players on the server
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws SteamCondenserException
	 */
	public void updatePlayerInfo()
			throws IOException, TimeoutException, SteamCondenserException {
		this.updatePlayerInfo(null);
	}

	/**
	 * Gets information about the players on the server
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws SteamCondenserException
	 */
	public void updatePlayerInfo(String rconPassword)
			throws IOException, TimeoutException, SteamCondenserException {
		this.handleResponseForRequest(GameServer.REQUEST_PLAYER);

		if(rconPassword != null && !this.playerHash.isEmpty()) {
			this.rconAuth(rconPassword);
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
	}

	/**
	 * Gets information about the setting of the server
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws SteamCondenserException
	 */
	public void updateRulesInfo()
			throws IOException, TimeoutException, SteamCondenserException {
		this.handleResponseForRequest(GameServer.REQUEST_RULES);
	}

	/**
	 * Gets basic server information
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws SteamCondenserException
	 */
	public void updateServerInfo()
			throws IOException, TimeoutException, SteamCondenserException {
		this.handleResponseForRequest(GameServer.REQUEST_INFO);
	}
}
