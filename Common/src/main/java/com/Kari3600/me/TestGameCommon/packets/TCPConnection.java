package com.Kari3600.me.TestGameCommon.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TCPConnection {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public InetAddress getHostAddress() {
        return socket.getInetAddress();
    }

    private static Socket getSocket(InetAddress host) {
        try {
            if (host == null) {
                throw new IllegalArgumentException("Host cannot be null.");
            }
            return new Socket(host.getHostName(), 2137);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Packet waitForPacket() {
        try {
            //System.out.println("Waiting for packet on IP: "+getHostAddress());
            Packet returnPacket = PacketManager.fromStream(ois);
            //System.out.println("Received packet "+returnPacket.getClass()+" on IP: "+getHostAddress());
            return returnPacket;
        } catch (IOException e) {
            System.exit(1);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendPacket(Packet packet) {
        try {
            //System.out.println("Sending packet "+packet.getClass()+" on IP: "+getHostAddress());
            packet.write(oos);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public CompletableFuture<Packet> sendPacketRequest(Packet packet) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                //System.out.println("Sending packet "+packet.getClass()+" on IP: "+getHostAddress());
                packet.write(oos);
                oos.flush();
                //System.out.println("Waiting for packet on IP: "+getHostAddress());
                Packet returnPacket = PacketManager.fromStream(ois);
                //System.out.println("Received packet "+returnPacket.getClass()+" on IP: "+getHostAddress());
                return returnPacket;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public TCPConnection(InetAddress host) {
        this(getSocket(host));
    }

    public TCPConnection(Socket socket) {
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
