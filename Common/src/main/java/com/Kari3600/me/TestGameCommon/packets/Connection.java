package com.Kari3600.me.TestGameCommon.packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.net.InetAddress;

public class Connection {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public InetAddress getHostAddress() {
        return socket.getInetAddress();
    }

    private static Socket getSocket(InetAddress host) {
        try {
            return new Socket(host.getHostName(), 2137);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Packet waitForPacket() {
        try {
            System.out.println("Waiting for packet on IP: "+getHostAddress());
            Packet returnPacket = PacketManager.fromStream(ois);
            System.out.println("Received packet "+returnPacket.getClass()+" on IP: "+getHostAddress());
            return returnPacket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendPacket(Packet packet) {
        try {
            System.out.println("Sending packet "+packet.getClass()+" on IP: "+getHostAddress());
            packet.write(oos);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<Packet> sendPacketRequest(Packet packet) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Sending packet "+packet.getClass()+" on IP: "+getHostAddress());
                packet.write(oos);
                oos.flush();
                System.out.println("Waiting for packet on IP: "+getHostAddress());
                Packet returnPacket = PacketManager.fromStream(ois);
                System.out.println("Received packet "+returnPacket.getClass()+" on IP: "+getHostAddress());
                return returnPacket;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
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
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Packet packet = PacketManager.fromStream(ois);
                    } catch (ClassNotFoundException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        */
    }
}
