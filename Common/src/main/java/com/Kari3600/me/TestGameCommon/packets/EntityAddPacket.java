package com.Kari3600.me.TestGameCommon.packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.Kari3600.me.TestGameCommon.Entity;

public class EntityAddPacket extends EntityInfoPacket {
    private static byte packetID = 1;
    private Class<? extends Entity> entityType;

    @Override
    protected void readData(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        entityType = (Class<? extends Entity>) stream.readObject();
    }

    @Override
    protected void writeData(ObjectOutputStream stream) throws IOException {
        super.writeData(stream);
        stream.writeByte(packetID);
        stream.writeObject(entityType);
    }
}
