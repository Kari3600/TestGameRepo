package com.Kari3600.me.TestGameCommon.packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.InetAddress;

public class Connection {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private static Socket getSocket(InetAddress host) {
        try {
            return new Socket(host.getHostName(), 2137);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Packet waitForPacket() {
        System.out.println("Waiting for packet on thread "+Thread.currentThread().getName());
        try {
            return Packet.fromStream(ois);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendPacket(Packet packet) {
        try {
            packet.toStream(oos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection(InetAddress host) {
        this(getSocket(host));
    }

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
