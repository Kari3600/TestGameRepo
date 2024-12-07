package com.Kari3600.me.TestGameCommon.packets;

import java.net.InetAddress;

public interface UDPPacketListener {
    public void onPacket(Packet packet, InetAddress address);
}