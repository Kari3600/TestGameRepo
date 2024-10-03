package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.IOException;

public class JoinGamePacket extends Packet {

    protected static final byte PacketID =  Packet.registerPacketClass(JoinGamePacket.class);

    @Override
    protected byte getPacketID() {
        return PacketID;
    }

    public static Packet fromStream(ObjectInputStream ois) throws IOException {
        return new JoinGamePacket();
    }
    
    public JoinGamePacket() {
        
    }
    
}