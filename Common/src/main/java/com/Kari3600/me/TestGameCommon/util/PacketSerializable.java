package com.Kari3600.me.TestGameCommon.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface PacketSerializable<T> {
    public void read(DataInputStream dis) throws IOException;
    public void write(DataOutputStream dos) throws IOException;
}
