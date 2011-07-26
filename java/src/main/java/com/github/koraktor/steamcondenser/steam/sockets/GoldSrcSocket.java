/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.github.koraktor.steamcondenser.exceptions.RCONBanException;
import com.github.koraktor.steamcondenser.exceptions.RCONNoAuthException;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacketFactory;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONGoldSrcRequestPacket;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONGoldSrcResponsePacket;

/**
 * This class represents a socket used to communicate with game servers based
 * on the GoldSrc engine (e.g. Half-Life, Counter-Strike)
 *
 * @author Sebastian Staudt
 */
public class GoldSrcSocket extends QuerySocket
{
    private boolean isHLTV;
    private long rconChallenge = -1;

    /**
     * Creates a new socket to communicate with the server on the given IP
     * address and port
     *
     * @param ipAddress Either the IP address or the DNS name of the server
     * @param portNumber The port the server is listening on
     * @throws IOException if the socket cannot be opened
     */
    public GoldSrcSocket(InetAddress ipAddress, int portNumber)
            throws IOException
    {
        super(ipAddress, portNumber);
        this.isHLTV = false;
    }

    /**
     * Creates a new socket to communicate with the server on the given IP
     * address and port
     *
     * @param ipAddress Either the IP address or the DNS name of the server
     * @param portNumber The port the server is listening on
     * @param isHLTV <code>true</code> if the target server is a HTLV instance.
     *        HLTV behaves slightly different for RCON commands, this flag
     *        increases compatibility.
     * @throws IOException if the socket cannot be opened
     */
    public GoldSrcSocket(InetAddress ipAddress, int portNumber, boolean isHLTV)
            throws IOException
    {
        super(ipAddress, portNumber);
        this.isHLTV = isHLTV;
    }

    /**
     * Reads a packet from the socket
     * <p>
     * The Source query protocol specifies a maximum packet size of 1,400
     * bytes. Bigger packets will be split over several UDP packets. This
     * method reassembles split packets into single packet objects.
     *
     * @return The packet replied from the server
     * @throws IOException if an error occurs while communicating with the
     *         server
     * @throws SteamCondenserException if the reply cannot be parsed
     * @throws TimeoutException if the request times out
     */
    public SteamPacket getReply()
            throws IOException, SteamCondenserException, TimeoutException
    {
        int bytesRead;
        SteamPacket packet;

        bytesRead = this.receivePacket(1400);

        if(this.packetIsSplit()) {
            byte[] splitData;
            int packetCount, packetNumber;
            int requestId;
            byte packetNumberAndCount;
            ArrayList<byte[]> splitPackets = new ArrayList<byte[]>();

            do {
                requestId = Integer.reverseBytes(this.buffer.getInt());
                packetNumberAndCount = this.buffer.get();
                packetCount = packetNumberAndCount & 0xF;
                packetNumber = (packetNumberAndCount >> 4) + 1;

                splitData = new byte[this.buffer.remaining()];
                this.buffer.get(splitData);
                splitPackets.ensureCapacity(packetCount);
                splitPackets.add(packetNumber - 1, splitData);

                Logger.getLogger("global").info("Received packet #" + packetNumber + " of " + packetCount + " for request ID " + requestId + ".");

                if(splitPackets.size() < packetCount) {
                    try {
                        bytesRead = this.receivePacket();
                    }
                    catch(TimeoutException e) {
                        bytesRead = 0;
                    }
                }
                else {
                    bytesRead = 0;
                }
            } while(bytesRead > 0 && this.packetIsSplit());

            packet = SteamPacketFactory.reassemblePacket(splitPackets);
        }
        else {
            packet = this.getPacketFromData();
        }

        Logger.getLogger("global").info("Received packet of type \"" + packet.getClass().getSimpleName() + "\"");

        return packet;
    }

    /**
     * Executes the given command on the server via RCON
     *
     * @param password The password to authenticate with the server
     * @param command The command to execute on the server
     * @return The response replied by the server
     * @see #rconChallenge
     * @see #rconSend
     * @throws IOException if an error occurs while communicating with the
     *         server
     * @throws RCONBanException if the IP of the local machine has been banned
     *         on the game server
     * @throws RCONNoAuthException if the password is incorrect
     * @throws SteamCondenserException if the reply cannot be parsed
     * @throws TimeoutException if the request times out
     */
    public String rconExec(String password, String command)
            throws IOException, TimeoutException, SteamCondenserException
    {
        if(this.rconChallenge == -1 || this.isHLTV) {
            this.rconGetChallenge();
        }

        this.rconSend("rcon " + this.rconChallenge + " " + password + " " + command);
        this.rconSend("rcon " + this.rconChallenge + " " + password);
        String response;
        if(this.isHLTV) {
            try {
                response = ((RCONGoldSrcResponsePacket)this.getReply()).getResponse();
            }
            catch(TimeoutException e) {
                response = "";
            }
        }
        else {
            response = ((RCONGoldSrcResponsePacket)this.getReply()).getResponse();
        }


        if(response.trim().equals("Bad rcon_password")) {
            throw new RCONNoAuthException();
        } else if(response.trim().equals("You have been banned from this server")) {
            throw new RCONBanException();
        }

        String responsePart;

        do {
            responsePart = ((RCONGoldSrcResponsePacket)this.getReply()).getResponse();
            response += responsePart;
        } while(responsePart.length() > 0);

        return response;
    }

    /**
     * Requests a challenge number from the server to be used for further
     * requests
     *
     * @see #rconSend
     * @throws IOException if an error occurs while communicating with the
     *         server
     * @throws NumberFormatException if the received response is not a valid
     *         challenge number
     * @throws RCONBanException if the IP of the local machine has been banned
     *         on the game server
     * @throws TimeoutException if the request times out
     */
    public void rconGetChallenge()
            throws IOException, TimeoutException, NumberFormatException, SteamCondenserException
    {
        this.rconSend("challenge rcon");

        String response = ((RCONGoldSrcResponsePacket)this.getReply()).getResponse().trim();
        if(response.equals("You have been banned from this server.")) {
            throw new RCONBanException();
        }

        this.rconChallenge = Long.valueOf(response.substring(14));
    }

    /**
     * Wraps the given command in a RCON request packet and send it to the
     * server
     *
     * @param command The RCON command to send to the server
     * @throws IOException if an error occured while writing to the socket
     */
    private void rconSend(String command)
            throws IOException
    {
        this.send(new RCONGoldSrcRequestPacket(command));
    }
}
