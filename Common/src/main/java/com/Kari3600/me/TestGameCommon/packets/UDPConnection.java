package com.Kari3600.me.TestGameCommon.packets;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPConnection {

    private DatagramSocket socket;
    private List<UDPPacketListener> listeners = new ArrayList<>();

    private static UDPConnection instance;

    private static DatagramSocket getSocket() {
        try {
            return new DatagramSocket(2138);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static UDPConnection getInstance() {
        return instance;
    }

    public void checkConnection(InetAddress address) {
        long totalTime = System.currentTimeMillis();
        long[] sendPings = new long[256];
        long[] receivePings = new long[256];

        CountDownLatch latch = new CountDownLatch(256);

        UDPPacketListener listener = new UDPPacketListener() {
            @Override
            public void onPacket(Packet p, InetAddress address) {
                if (!(p instanceof PacketPong)) {
                    return;
                }
                int ID = ((PacketPong) p).getID()-Byte.MIN_VALUE;
                receivePings[ID] = System.currentTimeMillis();

                latch.countDown();
            }
        };

        register(listener);

        for (int n=0;n<256;++n) {
            sendPings[n] = System.currentTimeMillis();
            sendPacket(new PacketPing().setID((byte) (n+Byte.MIN_VALUE)));
        }

        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        unregister(listener);
        
        System.out.println("Total time: "+(System.currentTimeMillis()-totalTime));
        List<Long> pings = new ArrayList<>();
        for (int n = 0; n < 256; ++n) {
            pings.add(receivePings[n]-sendPings[n]);
        }
        pings.sort(null);
        System.out.println("5%tile: "+pings.get(5));
        System.out.println("95%tile: "+pings.get(250) );
    }

    public void register(UDPPacketListener listener) {
        listeners.add(listener);
    }

    public void unregister(UDPPacketListener listener) {
        listeners.remove(listener);
    }

    public void sendPacket(Packet packet,InetAddress... addresses) {
        try {
            System.out.println("UDP Socket Address: " + socket.getLocalAddress() + ", Port: " + socket.getLocalPort());
            //System.out.println("Sending packet "+packet.getClass()+" on IP: "+getHostAddress());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            packet.write(new ObjectOutputStream(buffer));
            buffer.flush();
            byte[] fixedBuffer = buffer.toByteArray();
            if (fixedBuffer == null || fixedBuffer.length == 0) {
                throw new IllegalStateException("Buffer is null or empty.");
            }
            System.out.println(fixedBuffer);
            for (InetAddress address : addresses) {
                socket.send(new DatagramPacket(fixedBuffer, fixedBuffer.length, address, 2138));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UDPConnection() {
        instance = this;
        socket = getSocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //System.out.println("Waiting for packet on IP: "+getHostAddress());
                        byte[] fixedBuffer = new byte[65535];
                        DatagramPacket packet = new DatagramPacket(fixedBuffer, fixedBuffer.length);
                        socket.receive(packet);
                        Packet returnPacket = PacketManager.fromStream(new ObjectInputStream(new ByteArrayInputStream(fixedBuffer)));
                        for (UDPPacketListener listener : listeners) {
                            listener.onPacket(returnPacket,packet.getAddress());
                        }
                    } catch (IOException e) {
                        System.exit(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
