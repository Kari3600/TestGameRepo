package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class QueueCountPacket extends Packet {
    protected static final byte PacketID =  Packet.registerPacketClass(QueueCountPacket.class);

    @Override
    protected byte getPacketID() {
        return PacketID;
    }

    private byte count;

    public byte getCount() {
        return count;
    }

    public void setCount(byte count) {
        this.count = count;
    }

    public static Packet fromStream(ObjectInputStream stream) throws IOException {
        QueueCountPacket packet = new QueueCountPacket();
        packet.setCount(stream.readByte());
        return packet;
    }

    @Override
    protected void writeData(ObjectOutputStream stream) throws IOException {
        super.writeData(stream);
        stream.writeByte(count);
    }

    public QueueCountPacket(byte count) {
        this.count = count;
    }
    
    public QueueCountPacket() {
        
    }
}
