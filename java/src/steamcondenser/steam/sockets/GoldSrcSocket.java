/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */
package steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import steamcondenser.RCONNoAuthException;
import steamcondenser.SteamCondenserException;
import steamcondenser.UncompletePacketException;
import steamcondenser.steam.packets.SteamPacket;
import steamcondenser.steam.packets.SteamPacketFactory;
import steamcondenser.steam.packets.rcon.RCONGoldSrcRequestPacket;
import steamcondenser.steam.packets.rcon.RCONGoldSrcResponsePacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class GoldSrcSocket extends QuerySocket
{
    private boolean isHLTV;
    private long rconChallenge = -1;

    /**
     *
     * @param ipAddress
     * @param portNumber
     * @throws IOException
     */
    public GoldSrcSocket(InetAddress ipAddress, int portNumber)
            throws IOException
    {
        super(ipAddress, portNumber);
        this.isHLTV = false;
    }

    /**
     * 
     * @param ipAddress
     * @param portNumber
     * @param isHLTV
     * @throws IOException
     */
    public GoldSrcSocket(InetAddress ipAddress, int portNumber, boolean isHLTV)
            throws IOException
    {
        super(ipAddress, portNumber);
        this.isHLTV = isHLTV;
    }

    /**
     * @return The SteamPacket received from the server
     * @throws IOException
     * @throws SteamCondenserException
     * @throws TimeoutException
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
            Vector<byte[]> splitPackets = new Vector<byte[]>();

            do {
                // Parsing of split packet headers
                requestId = Integer.reverseBytes(this.buffer.getInt());
                packetNumberAndCount = this.buffer.get();
                packetCount = packetNumberAndCount & 0xF;
                packetNumber = (packetNumberAndCount >> 4) + 1;

                // Caching of split packet Data
                splitData = new byte[this.buffer.remaining()];
                this.buffer.get(splitData);
                splitPackets.setSize(packetCount);
                splitPackets.set(packetNumber - 1, splitData);

                Logger.getLogger("global").info("Received packet #" + packetNumber + " of " + packetCount + " for request ID " + requestId + ".");

                // Receiving the next packet
                try {
                    bytesRead = this.receivePacket();
                }
                catch(TimeoutException e) {
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
     * Sends a RCON command with the specified password to a GoldSrc server
     * @param password RCON password to use
     * @param command RCON command to send to the server
     * @return The response send by the server
     * @throws IOException
     * @throws TimeoutException
     * @throws SteamCondenserException 
     * @throws UncompletePacketException 
     */
    public String rconExec(String password, String command)
            throws IOException, TimeoutException, SteamCondenserException
    {
        if(this.rconChallenge == -1) {
            this.rconGetChallenge();
        }

        this.rconSend("rcon " + this.rconChallenge + " " + password + " " + command);
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
        

        if(response.trim().equals("Bad rcon_password") || response.trim().equals("You have been banned from this server")) {
            throw new RCONNoAuthException();
        }

        String responsePart;

        try {
            do {
                responsePart = ((RCONGoldSrcResponsePacket)this.getReply()).getResponse();
                response += responsePart;
            } while(true);
        }
        catch(TimeoutException e) {
        }

        return response;
    }

    /**
     * @throws SteamCondenserException 
     * @throws NumberFormatException 
     * 
     */
    public void rconGetChallenge()
            throws IOException, TimeoutException, NumberFormatException, SteamCondenserException
    {
        this.rconSend("challenge rcon");

        String response = ((RCONGoldSrcResponsePacket)this.getReply()).getResponse().trim();
        if(response.equals("You have been banned from this server.")) {
            throw new RCONNoAuthException();
        }

        this.rconChallenge = Long.valueOf(response.substring(14));
    }

    private void rconSend(String command)
            throws IOException
    {
        this.send(new RCONGoldSrcRequestPacket(command));
    }
}
