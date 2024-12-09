package com.Kari3600.me.TestGameCommon.packets;

import java.net.InetAddress;

public class UDPSingleConnection extends UDPConnection {

    private final InetAddress host;
    
    public long checkConnection() {
        return checkConnection(host);
    }

    public void sendPacket(Packet packet) {
        sendPacket(packet, host);
    }

    @Override
    protected void onPacket(Packet packet, InetAddress address) {
        if (!address.equals(host)) return;
        super.onPacket(packet, address);
    }

    public UDPSingleConnection(InetAddress host) {
        super();
        this.host = host;
    }
}
