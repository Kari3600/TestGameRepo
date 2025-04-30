package com.Kari3600.me.TestGameCommon.packets;

import java.net.InetSocketAddress;

public interface PacketListener {
    public void onPacket(Packet packet, InetSocketAddress address);
}