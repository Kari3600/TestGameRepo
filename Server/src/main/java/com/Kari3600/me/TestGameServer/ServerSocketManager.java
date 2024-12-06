package com.Kari3600.me.TestGameServer;

import java.net.ServerSocket;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.Kari3600.me.TestGameCommon.packets.Connection;
import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginRequest;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginResponse;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginResult;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginTask;
import com.Kari3600.me.TestGameCommon.packets.PacketQueueJoin;
import com.Kari3600.me.TestGameCommon.packets.PacketPing;
import com.Kari3600.me.TestGameCommon.packets.PacketPong;
import com.Kari3600.me.TestGameCommon.packets.PacketRegisterRequest;
import com.Kari3600.me.TestGameCommon.packets.PacketRegisterResult;
import com.Kari3600.me.TestGameCommon.util.EncryptionUtil;

public class ServerSocketManager implements Runnable {
    private static ServerSocket server; 
    private static int port = 2137;

    private void login(Connection conn, PacketLoginRequest packet) {
        String salt = EncryptionUtil.encrypt(String.valueOf(System.currentTimeMillis()));
        CompletableFuture<Packet> packetFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return conn.sendPacketTCPRequest(new PacketLoginTask().setSalt(salt)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
        CompletableFuture<ResultSet> databaseFuture = DatabaseManager.executeQuery("SELECT `Password` FROM `Users` WHERE `Username` = ?",packet.getUsername());
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(packetFuture, databaseFuture);
        try {
            combinedFuture.join();
            Packet returnPacket = packetFuture.get();
            ResultSet rs = databaseFuture.get();
            if (!(returnPacket instanceof PacketLoginResponse)) {
                throw new Exception("Wrong packet");
            }
            PacketLoginResponse response = (PacketLoginResponse) returnPacket;
            System.out.println("Received login response.");
            if (!rs.next()) {
                conn.sendPacketTCP(new PacketLoginResult().setStatus((byte) 1));
                return;
            }
            if (!response.getPassword().equals(EncryptionUtil.encrypt(salt+rs.getString("Password")))) {
                conn.sendPacketTCP(new PacketLoginResult().setStatus((byte) 2));
                return;
            }
            conn.sendPacketTCP(new PacketLoginResult().setStatus((byte) 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(Connection conn, PacketRegisterRequest packet) {
        CompletableFuture<Integer> databaseFuture = DatabaseManager.executeUpdate("INSERT IGNORE INTO users (username, password) VALUES (?, ?)",packet.getUsername(),packet.getPassword());
        databaseFuture.thenRun(() -> {
            try {
                boolean success = databaseFuture.get()==1;
                if (!success) {
                    conn.sendPacketTCP(new PacketRegisterResult().setStatus((byte) 1));
                    return;
                }
                conn.sendPacketTCP(new PacketRegisterResult().setStatus((byte) 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void run() {
        try {
            server = new ServerSocket(port);
            while(true){
                Connection conn = new Connection(server.accept());
                System.out.println("User connected on IP: "+conn.getHostAddress());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            Packet packet = conn.waitForPacketTCP();
                            if (packet instanceof PacketPing) {
                                conn.sendPacketTCP(new PacketPong().setID(((PacketPing) packet).getID()));
                            }
                            if (packet instanceof PacketLoginRequest) {
                                login(conn, (PacketLoginRequest) packet);
                            }
                            if (packet instanceof PacketRegisterRequest) {
                                register(conn, (PacketRegisterRequest) packet);
                            }
                            if (packet instanceof PacketQueueJoin) {
                                PacketQueueJoin joinQueuePacket = (PacketQueueJoin) packet;
                                System.out.println("Player joined");
                                Queue.queuePlayer(conn);
                            }
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
