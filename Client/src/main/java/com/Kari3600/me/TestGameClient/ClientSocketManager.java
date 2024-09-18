package com.Kari3600.me.TestGameClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import com.Kari3600.me.TestGameCommon.PacketType;

public class ClientSocketManager {

    private UUID playerID;
    private InetAddress host;

    public boolean tryToConnect() {
        try {
            Socket socket = new Socket(host.getHostName(), 2137);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeByte(PacketType.CONNECT);
            oos.writeObject(playerID);
            oos.flush();

            boolean success = ois.readBoolean();
            System.out.println("Received success status: " + success);

            ois.close();
            oos.close();
            socket.close();

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ClientSocketManager() {
        try {
            host = InetAddress.getLocalHost();
        } catch (Exception e) {
            host = null;
        }
        playerID = UUID.randomUUID();
    }

}
