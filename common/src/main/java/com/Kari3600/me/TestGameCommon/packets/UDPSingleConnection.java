package com.Kari3600.me.TestGameCommon.packets;

import com.Kari3600.me.TestGameCommon.packets.Packet;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UDPSingleConnection extends UDPConnection {

    private final InetSocketAddress host;
    
    public long checkConnection() {
        return checkConnection(host);
    }

    public void sendPacket(Packet packet) {
        sendPacket(packet, host);
    }

    @Override
    protected void onPacket(Packet packet, InetSocketAddress address) {
        if (!address.equals(host)) return;
        super.onPacket(packet, address);
    }

    public UDPSingleConnection(InetSocketAddress host) {
        super();
        this.host = host;
    }
}
