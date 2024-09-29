package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashMap;

public abstract class Packet {

    protected static HashMap<Byte,Class<? extends Packet>> packets = new HashMap<>();
    static {
        Packet.packets.put((byte) 1, JoinQueuePacket.class); 
        Packet.packets.put((byte) 2, QueueCountPacket.class);
        Packet.packets.put((byte) 3, StartQueuePacket.class);
        Packet.packets.put((byte) 4, JoinGamePacket.class);
    }

    public static Packet fromStream(ObjectInputStream ois) throws IOException {
        byte packetId = ois.readByte();
        Class<? extends Packet> packetClass = packets.get(packetId);
        if (packetClass == null) {
            throw new IOException("Unknown packet ID: " + packetId);
        }
        try {
            Packet packet = packetClass.getConstructor().newInstance();
            packet.readData(ois);
            return packet;
        } catch (ReflectiveOperationException e) {
            throw new IOException("Failed to create packet instance", e);
        }
    }

    protected abstract void readData(ObjectInputStream stream) throws IOException,ClassNotFoundException;

    protected abstract void toStream(ObjectOutputStream stream) throws IOException,ClassNotFoundException;
}
