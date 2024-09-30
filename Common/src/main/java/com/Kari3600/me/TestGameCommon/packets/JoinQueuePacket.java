package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.UUID;

public class JoinQueuePacket extends Packet {
    private static byte packetID = 1;
    private UUID playerID;

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    @Override
    protected void readData(ObjectInputStream stream) throws IOException,ClassNotFoundException {
        playerID = (UUID) stream.readObject();
    }

    @Override
    protected void writeData(ObjectOutputStream stream) throws IOException {
        stream.writeByte(packetID);
        stream.writeObject(playerID);
    }
    public JoinQueuePacket(UUID playerID) {
        this.playerID = playerID;
    }
    public JoinQueuePacket() {
        
    }
    
}
