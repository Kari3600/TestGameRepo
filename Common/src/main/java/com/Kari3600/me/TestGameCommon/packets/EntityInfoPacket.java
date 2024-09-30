package com.Kari3600.me.TestGameCommon.packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.UUID;

public class EntityInfoPacket extends Packet {
    private static byte packetID = 5;

    private static HashMap<Byte,Class<? extends EntityInfoPacket>> packets = new HashMap<>();
    static {
        packets.put((byte) 1, EntityAddPacket.class); 
    }

    private UUID entityID;

    public UUID getEntityID() {
        return entityID;
    }

    public void setEntityID(UUID entityID) {
        this.entityID = entityID;
    }

    @Override
    protected void readData(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        entityID = (UUID) stream.readObject();
        byte subpacketID = stream.readByte();
        Class<? extends EntityInfoPacket> packetClass = packets.get(subpacketID);
        if (packetClass == null) {
            throw new IOException("Unknown packet ID: " + subpacketID);
        }
        try {
            Packet packet = packetClass.getConstructor().newInstance();
            packet.readData(ois);
            return packet;
        } catch (ReflectiveOperationException e) {
            throw new IOException("Failed to create packet instance", e);
        }
    }

    @Override
    protected void writeData(ObjectOutputStream stream) throws IOException {
        stream.writeByte(packetID);
        stream.writeObject(entityID);
    }
    public EntityInfoPacket(UUID entityID) {
        this.entityID = entityID;
    }
    public EntityInfoPacket() {
        
    }
    
}
