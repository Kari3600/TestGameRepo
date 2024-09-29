package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class QueueCountPacket extends Packet {
    private static byte packetID = 2;

    private byte count;

    public byte getCount() {
        return count;
    }

    public void setCount(byte count) {
        this.count = count;
    }

    @Override
    protected void readData(ObjectInputStream stream) throws IOException,ClassNotFoundException {
        count = stream.readByte();
    }

    @Override
    protected void toStream(ObjectOutputStream stream) throws IOException,ClassNotFoundException {
        stream.writeByte(packetID);
        stream.writeByte(count);
        stream.flush();
    }

    public QueueCountPacket(byte count) {
        this.count = count;
    }
    
    public QueueCountPacket() {
        
    }
}
