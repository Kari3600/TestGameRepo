package com.Kari3600.me.TestGameCommon.packets;

public class PingPong implements Packet {
    private final byte ping;
    private final byte id;

    public byte getPing() {
        return ping;
    }

    public byte getId() {
        return id;
    }

    public PingPong(byte ping, byte id) {
        this.ping = ping;
        this.id = id;
    }
}
