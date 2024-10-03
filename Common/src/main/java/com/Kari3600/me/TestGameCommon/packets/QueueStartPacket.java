package com.Kari3600.me.TestGameCommon.packets;

import java.io.IOException;
import java.io.ObjectInputStream;

public class QueueStartPacket extends Packet {
    protected static final byte PacketID =  Packet.registerPacketClass(QueueStartPacket.class);

    @Override
    protected byte getPacketID() {
        return PacketID;
    }

    public static Packet fromStream(ObjectInputStream stream) throws IOException {
        return new QueueStartPacket();
    }
    
    public QueueStartPacket() {
        
    }
}
