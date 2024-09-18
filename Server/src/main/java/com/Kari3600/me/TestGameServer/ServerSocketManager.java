package com.Kari3600.me.TestGameServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import com.Kari3600.me.TestGameCommon.PacketType;

public class ServerSocketManager implements Runnable {
    private static ServerSocket server; 
    private static int port = 2137;

    private boolean connectPlayer(UUID playerID) {
        System.out.println("Connecting player with ID "+playerID);
        return true;
    }

    public void run() {
        try {
            server = new ServerSocket(port);
            while(true){
                System.out.println("Waiting for the client request");
                Socket socket = server.accept();

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                byte packetID = ois.readByte();
                System.out.println("Packet ID: " + packetID);
                switch (packetID) {
                    case PacketType.CONNECT:
                        UUID playerID = (UUID) ois.readObject();
                        boolean success = connectPlayer(playerID);
                        oos.writeBoolean(success);
                        oos.flush();
                        break;
                
                    default:
                        System.out.println("Unknown packet ID: " + packetID);
                        break;
                }
                ois.close();
                oos.close();
                socket.close();
            }
            //System.out.println("Shutting down Socket server!!");
            //server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
