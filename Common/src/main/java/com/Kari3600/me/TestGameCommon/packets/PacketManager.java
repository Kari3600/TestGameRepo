package com.Kari3600.me.TestGameCommon.packets;

import java.io.ObjectInputStream;
import java.util.UUID;
import java.io.IOException;

import com.Kari3600.me.TestGameAnnotations.packets.CreatePacket;
import com.Kari3600.me.TestGameCommon.util.Vector2;
import com.Kari3600.me.TestGameCommon.util.Vector3;

@CreatePacket(name = "", parent = "", fields = {})
@CreatePacket(name = "Ping",parent = "", fields = {
    @CreatePacket.Field(type = Byte.class, name = "ID")
})
@CreatePacket(name = "Pong",parent = "", fields = {
    @CreatePacket.Field(type = Byte.class, name = "ID")
})
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
@CreatePacket(name = "QueueJoin", parent = "", fields = {})
@CreatePacket(name = "QueueCount", parent = "", fields = {
    @CreatePacket.Field(type = Byte.class, name = "count")
})
@CreatePacket(name = "QueueStart", parent = "", fields = {})
@CreatePacket(name = "Game", parent = "", fields = {
    @CreatePacket.Field(type = Long.class, name = "tick")
})
@CreatePacket(name = "Entity", parent = "Game", fields = {
    @CreatePacket.Field(type = UUID.class, name = "entityID")
})
@CreatePacket(name = "EntityAdd", parent = "Entity", fields = {
    @CreatePacket.Field(type = Vector3.class, name = "position")
})
@CreatePacket(name = "EntityPath", parent = "Entity", fields = {
    @CreatePacket.Field(type = Vector2.class, name = "destination")
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
