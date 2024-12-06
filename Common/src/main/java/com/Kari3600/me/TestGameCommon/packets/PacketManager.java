package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.io.IOException;

import com.Kari3600.me.TestGameAnnotations.packets.CreatePacket;

@CreatePacket(name = "", parent = "", fields = {})
@CreatePacket(name = "LoginRequest", parent = "", fields = {
    @CreatePacket.Field(type = String.class, name = "username")
})
@CreatePacket(name = "LoginTask", parent = "", fields = {
    @CreatePacket.Field(type = String.class, name = "salt")
})
@CreatePacket(name = "LoginResponse", parent = "", fields = {
    @CreatePacket.Field(type = String.class, name = "password")
})
@CreatePacket(name = "LoginResult", parent = "", fields = {
    @CreatePacket.Field(type = Byte.class, name = "status")
})
@CreatePacket(name = "RegisterRequest", parent = "", fields = {
    @CreatePacket.Field(type = String.class, name = "username"),
    @CreatePacket.Field(type = String.class, name = "password")
})
@CreatePacket(name = "RegisterResult", parent = "", fields = {
    @CreatePacket.Field(type = Byte.class, name = "status")
})
public abstract class PacketManager {
    public static Packet fromStream(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        Packet packet = Packet.read(ois);
        if (packet == null) {
            throw new IOException("Invalid packet");
        }
        return packet;
    }
}
