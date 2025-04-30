package com.Kari3600.me.TestGameCommon.packets;

import com.Kari3600.me.RapidSerializer.serializers.RapidSerializer;

import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class UDPConnection {
    private final RapidSerializer<Packet> packetSerializer = RapidSerializer.getSerializer(Packet.class);

    private DatagramSocket socket;
    private List<PacketListener> listeners = new ArrayList<>();

    private static UDPConnection instance;

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for(byte b: bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static DatagramSocket getSocket(Integer port) {
        try {
            if (port == null) {
                return new DatagramSocket();
            } else {
                return new DatagramSocket(port);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPort() {
        return socket.getPort();
    }
    
    public static UDPConnection getInstance() {
        return instance;
    }

    public long checkConnection(InetSocketAddress address) {
        long totalTime = System.currentTimeMillis();
        long[] sendPings = new long[256];
        long[] receivePings = new long[256];

        CountDownLatch latch = new CountDownLatch(256);

        PacketListener listener = new PacketListener() {
            @Override
            public void onPacket(Packet p, InetSocketAddress address) {
                if (!(p instanceof PingPong)) {
                    return;
                }
                int ID = ((PingPong) p).getId()-Byte.MIN_VALUE;
                receivePings[ID] = System.currentTimeMillis();

                latch.countDown();
            }
        };

        register(listener);

        for (int n=0;n<256;++n) {
            sendPings[n] = System.currentTimeMillis();
            sendPacket(new PingPong((byte) 0, (byte) (n+Byte.MIN_VALUE)),address);
        }

        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        unregister(listener);
        
        System.out.println("Total time: "+(System.currentTimeMillis()-totalTime));
        List<Long> pings = new ArrayList<>();
        for (int n = 0; n < 256; ++n) {
            pings.add(receivePings[n]-sendPings[n]);
        }
        pings.sort(null);
        System.out.println("5%tile: "+Math.max(pings.get(5),0));
        System.out.println("95%tile: "+pings.get(250) );
        return pings.get(250)-Math.max(pings.get(5),0);
    }

    public void register(PacketListener listener) {
        listeners.add(listener);
    }

    public void unregister(PacketListener listener) {
        listeners.remove(listener);
    }

    public synchronized void sendPacket(Packet packet, InetSocketAddress address) {
        sendPacket(packet, Collections.singleton(address));
    }

    public synchronized void sendPacket(Packet packet, Collection<InetSocketAddress> addresses) {
        try {
            //System.out.println("UDP Socket Address: " + socket.getLocalAddress() + ", Port: " + socket.getLocalPort());
            //System.out.println("Sending packet "+packet.getClass()+" on IP: "+getHostAddress());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            packetSerializer.serialize(new DataOutputStream(buffer), packet);
            buffer.flush();
            byte[] fixedBuffer = buffer.toByteArray();
            if (fixedBuffer == null || fixedBuffer.length == 0) {
                throw new IllegalStateException("Buffer is null or empty.");
            }
            //System.out.println(bytesToHex(fixedBuffer));
            for (InetSocketAddress address : addresses) {
                //System.out.println("Sending packet to socket on "+address);
                socket.send(new DatagramPacket(fixedBuffer, fixedBuffer.length, address));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onPacket(Packet packet, InetSocketAddress address) {
        for (PacketListener listener : listeners) {
            listener.onPacket(packet,address);
        }
    }

    public UDPConnection() {
        this(null);
    }

    public UDPConnection(Integer port) {
        instance = this;
        socket = getSocket(port);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //System.out.println("Waiting for UDP packet");
                        byte[] fixedBuffer = new byte[65535];
                        DatagramPacket packet = new DatagramPacket(fixedBuffer, fixedBuffer.length);
                        socket.receive(packet);
                        Packet returnPacket = packetSerializer.deserialize(new DataInputStream(new ByteArrayInputStream(fixedBuffer)));
                        onPacket(returnPacket, (InetSocketAddress) packet.getSocketAddress());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        }).start();
    }

}
