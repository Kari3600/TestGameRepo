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

public class Connection {

    private Socket socketTCP;
    private DatagramSocket socketUDP;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public InetAddress getHostAddress() {
        return socketTCP.getInetAddress();
    }

    public void checkConnection() {
        long totalTime = System.currentTimeMillis();
        long[] sendPings = new long[256];
        long[] receivePings = new long[256];

        CompletableFuture<Void> futureSend = CompletableFuture.runAsync(() -> {
            for (int n=0;n<256;++n) {
                sendPings[n] = System.currentTimeMillis();
                sendPacketUDP(new PacketPing().setID((byte) (n+Byte.MIN_VALUE)));
            }
        });

        CompletableFuture<Void> futureReceive = CompletableFuture.runAsync(() -> {
            for (int n = 0; n < 256; ++n) {
                if ((n+1)%16 == 0) {
                    System.out.println((n+1));
                }
                Packet p = waitForPacketUDP();
                if (!(p instanceof PacketPong)) {
                    break;
                }
                int ID = ((PacketPong) p).getID()-Byte.MIN_VALUE;
                receivePings[ID] = System.currentTimeMillis();
            }
        });

        CompletableFuture.allOf(futureReceive,futureSend).join();
        
        System.out.println("Total time: "+(System.currentTimeMillis()-totalTime));
        List<Long> pings = new ArrayList<>();
        for (int n = 0; n < 256; ++n) {
            pings.add(receivePings[n]-sendPings[n]);
        }
        pings.sort(null);
        System.out.println("5%tile: "+pings.get(5));
        System.out.println("95%tile: "+pings.get(250) );
    }

    private static Socket getSocketTCP(InetAddress host) {
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

    private static DatagramSocket getSocketUDP(InetAddress host) {
        try {
            if (host == null) {
                throw new IllegalArgumentException("Host cannot be null.");
            }
            return new DatagramSocket(2138, host);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Packet waitForPacketTCP() {
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

    public Packet waitForPacketUDP() {
        try {
            //System.out.println("Waiting for packet on IP: "+getHostAddress());
            byte[] fixedBuffer = new byte[65535];
            socketUDP.receive(new DatagramPacket(fixedBuffer, fixedBuffer.length));
            Packet returnPacket = PacketManager.fromStream(new ObjectInputStream(new ByteArrayInputStream(fixedBuffer)));
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

    public void sendPacketTCP(Packet packet) {
        try {
            //System.out.println("Sending packet "+packet.getClass()+" on IP: "+getHostAddress());
            packet.write(oos);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacketUDP(Packet packet) {
        try {
            System.out.println("UDP Socket Address: " + socketUDP.getLocalAddress() + ", Port: " + socketUDP.getLocalPort());
            //System.out.println("Sending packet "+packet.getClass()+" on IP: "+getHostAddress());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            packet.write(new ObjectOutputStream(buffer));
            buffer.flush();
            byte[] fixedBuffer = buffer.toByteArray();
            if (fixedBuffer == null || fixedBuffer.length == 0) {
                throw new IllegalStateException("Buffer is null or empty.");
            }
            System.out.println(fixedBuffer);
            socketUDP.send(new DatagramPacket(fixedBuffer, fixedBuffer.length,socketUDP.getRemoteSocketAddress()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<Packet> sendPacketTCPRequest(Packet packet) {
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

    public Connection(InetAddress host) {
        this(getSocketTCP(host));
    }

    public Connection(Socket socketTCP) {
        this.socketTCP = socketTCP;
        this.socketUDP = getSocketUDP(socketTCP.getInetAddress());
        try {
            oos = new ObjectOutputStream(socketTCP.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socketTCP.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
