package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class JoinGamePacket extends Packet {
    private static byte packetID = 4;

    @Override
    protected void readData(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        
    }

    @Override
    protected void writeData(ObjectOutputStream stream) throws IOException {
        stream.writeByte(packetID);
    }
    
    public JoinGamePacket() {
        
    }
    
}