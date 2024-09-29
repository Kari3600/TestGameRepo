package com.Kari3600.me.TestGameServer;

import java.net.ServerSocket;

import com.Kari3600.me.TestGameCommon.packets.Connection;
import com.Kari3600.me.TestGameCommon.packets.JoinQueuePacket;
import com.Kari3600.me.TestGameCommon.packets.Packet;

public class ServerSocketManager implements Runnable {
    private static ServerSocket server; 
    private static int port = 2137;

    public void run() {
        try {
            server = new ServerSocket(port);
            while(true){
                System.out.println("Waiting for the client request");
                Connection conn = new Connection(server.accept());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Packet packet = conn.waitForPacket();
                        if (packet instanceof JoinQueuePacket) {
                            JoinQueuePacket joinQueuePacket = (JoinQueuePacket) packet;
                            System.out.println("Player with ID "+joinQueuePacket.getPlayerID()+" just joined");
                            Queue.queuePlayer(conn);
                        }
                    }
                }).start(); 
            }
            //System.out.println("Shutting down Socket server!!");
            //server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
