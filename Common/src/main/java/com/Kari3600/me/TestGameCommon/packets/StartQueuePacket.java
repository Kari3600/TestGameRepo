package com.Kari3600.me.TestGameCommon.packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StartQueuePacket extends Packet {
    private static byte packetID = 3;

    @Override
    protected void readData(ObjectInputStream stream) throws IOException,ClassNotFoundException {
        
    }

    @Override
    protected void toStream(ObjectOutputStream stream) throws IOException,ClassNotFoundException {
        stream.writeByte(packetID);
        stream.flush();
    }
    
    public StartQueuePacket() {
        
    }
}
