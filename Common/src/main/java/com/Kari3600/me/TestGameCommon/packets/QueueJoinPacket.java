package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.UUID;

public class QueueJoinPacket extends Packet {

    protected static final byte PacketID =  Packet.registerPacketClass(QueueJoinPacket.class);

    @Override
    protected byte getPacketID() {
        return PacketID;
    }

    private UUID playerID;

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public static Packet fromStream(ObjectInputStream ois) throws IOException {
        QueueJoinPacket packet = new QueueJoinPacket();
        try {
            packet.setPlayerID((UUID) ois.readObject());
        } catch (ClassNotFoundException e) {
            throw new IOException("Bad packet.",e);
        }
        return packet;
    }

    @Override
    protected void writeData(ObjectOutputStream stream) throws IOException {
        super.writeData(stream);
        stream.writeObject(playerID);
    }
    public QueueJoinPacket(UUID playerID) {
        this.playerID = playerID;
    }
    public QueueJoinPacket() {
        
    }
    
}
