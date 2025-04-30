package com.Kari3600.me.TestGameServer;

import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.account.*;
import com.Kari3600.me.TestGameCommon.packets.PingPong;
import com.Kari3600.me.TestGameCommon.packets.TCPConnection;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;
import com.Kari3600.me.TestGameCommon.packets.PacketListener;
import com.Kari3600.me.TestGameCommon.Player;
import com.Kari3600.me.TestGameCommon.Champions.Braum.Braum;
import com.Kari3600.me.TestGameCommon.queue.QueueJoin;
import com.Kari3600.me.TestGameCommon.util.EncryptionUtil;

public class ServerSocketManager implements Runnable, PacketListener {
    private static ServerSocket server; 
    private static int port = 2137;

    private void login(TCPConnection conn, LoginRequest packet) {
        String salt = EncryptionUtil.encrypt(String.valueOf(System.currentTimeMillis()));
        CompletableFuture<Packet> packetFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return conn.sendPacketRequest(new LoginTask(salt)).get();
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
            if (!(returnPacket instanceof LoginResponse)) {
                throw new Exception("Wrong packet");
            }
            LoginResponse response = (LoginResponse) returnPacket;
            System.out.println("Received login response.");
            if (!rs.next()) {
                conn.sendPacket(new LoginResult((byte) 1));
                return;
            }
            if (!response.getPassword().equals(EncryptionUtil.encrypt(salt+rs.getString("Password")))) {
                conn.sendPacket(new LoginResult((byte) 2));
                return;
            }
            conn.sendPacket(new LoginResult((byte) 0));
            Player pl = new Player(packet.getUsername(),conn);
            // TODO temporary solution
            pl.setChampion(Braum.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(TCPConnection conn, RegisterRequest packet) {
        CompletableFuture<Integer> databaseFuture = DatabaseManager.executeUpdate("INSERT IGNORE INTO users (username, password) VALUES (?, ?)",packet.getUsername(),packet.getPassword());
        databaseFuture.thenRun(() -> {
            try {
                boolean success = databaseFuture.get()==1;
                if (!success) {
                    conn.sendPacket(new RegisterResult((byte) 1));
                    return;
                }
                conn.sendPacket(new RegisterResult((byte) 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void run() {
        try {
            server = new ServerSocket(port);
            while(true){
                TCPConnection conn = new TCPConnection(server.accept());
                System.out.println("User connected on IP: "+conn.getHostAddress());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            Packet packet = conn.waitForPacket();
                            if (packet instanceof LoginRequest) {
                                login(conn, (LoginRequest) packet);
                            }
                            if (packet instanceof RegisterRequest) {
                                register(conn, (RegisterRequest) packet);
                            }
                            if (packet instanceof QueueJoin) {
                                QueueJoin joinQueuePacket = (QueueJoin) packet;
                                System.out.println("Player joined");
                                Queue.queuePlayer(Player.getByAddress(conn.getHostAddress()));
                                // TODO check if player is in the game
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

    @Override
    public void onPacket(Packet packet, InetSocketAddress address) {
        if (packet instanceof PingPong) {
            UDPConnection.getInstance().sendPacket(new PingPong((byte) 1, ((PingPong) packet).getId()), address);
        }
    }
}
