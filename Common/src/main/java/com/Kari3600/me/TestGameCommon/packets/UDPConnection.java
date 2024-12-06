package com.Kari3600.me.TestGameCommon.packets;

import java.net.InetAddress;
import java.net.DatagramSocket;

public class UDPConnection {

    private DatagramSocket socket;

    private static DatagramSocket getSocket(InetAddress host) {
        try {
            return new DatagramSocket(2137);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UDPConnection(InetAddress host) {
        socket = getSocket(host);
    }

}
