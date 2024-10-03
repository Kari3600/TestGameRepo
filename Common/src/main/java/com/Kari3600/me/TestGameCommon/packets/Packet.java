package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Packet {

    private static ArrayList<Class<? extends Packet>> packets = new ArrayList<>();
    private static HashMap<Class<? extends Packet>,Method> packetsMethod = new HashMap<>();

    protected static byte registerPacketClass(Class<? extends Packet> packetClass) {
        packets.add(packetClass);
        try {
            packetsMethod.put(packetClass, packetClass.getMethod("fromStream", ObjectInputStream.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (byte) packets.indexOf(packetClass);
    }

    protected abstract byte getPacketID();

    public static Packet fromStream(ObjectInputStream ois) throws IOException {
        byte packetID = ois.readByte();
        Class<? extends Packet> packetClass = packets.get(packetID);
        if (packetClass == null) {
            throw new IOException("Cannot find Class of packet ID: " + packetID);
        }
        try {
            return (Packet) packetsMethod.get(packetClass).invoke(null, ois);
        } catch (ReflectiveOperationException e) {
            throw new IOException("Failed to create packet instance", e);
        }
    }

    public void toStream(ObjectOutputStream stream) throws IOException {
        writeData(stream);
        stream.flush();
    }

    protected void writeData(ObjectOutputStream stream) throws IOException {
        byte packetID = getPacketID();
        stream.writeByte(packetID);
    }
}
