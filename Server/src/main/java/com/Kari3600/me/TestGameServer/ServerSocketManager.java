package com.Kari3600.me.TestGameServer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.Kari3600.me.TestGameCommon.packets.TCPConnection;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;
import com.Kari3600.me.TestGameCommon.packets.UDPPacketListener;
import com.Kari3600.me.TestGameCommon.Player;
import com.Kari3600.me.TestGameCommon.Champions.Braum.Braum;
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

public class ServerSocketManager implements Runnable, UDPPacketListener {
    private static ServerSocket server; 
    private static int port = 2137;

    private void login(TCPConnection conn, PacketLoginRequest packet) {
        String salt = EncryptionUtil.encrypt(String.valueOf(System.currentTimeMillis()));
        CompletableFuture<Packet> packetFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return conn.sendPacketRequest(new PacketLoginTask().setSalt(salt)).get();
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
                conn.sendPacket(new PacketLoginResult().setStatus((byte) 1));
                return;
            }
            if (!response.getPassword().equals(EncryptionUtil.encrypt(salt+rs.getString("Password")))) {
                conn.sendPacket(new PacketLoginResult().setStatus((byte) 2));
                return;
            }
            conn.sendPacket(new PacketLoginResult().setStatus((byte) 0));
            Player pl = new Player(packet.getUsername(),conn);
            // TODO temporary solution
            pl.setChampion(Braum.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(TCPConnection conn, PacketRegisterRequest packet) {
        CompletableFuture<Integer> databaseFuture = DatabaseManager.executeUpdate("INSERT IGNORE INTO users (username, password) VALUES (?, ?)",packet.getUsername(),packet.getPassword());
        databaseFuture.thenRun(() -> {
            try {
                boolean success = databaseFuture.get()==1;
                if (!success) {
                    conn.sendPacket(new PacketRegisterResult().setStatus((byte) 1));
                    return;
                }
                conn.sendPacket(new PacketRegisterResult().setStatus((byte) 0));
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
                            if (packet instanceof PacketLoginRequest) {
                                login(conn, (PacketLoginRequest) packet);
                            }
                            if (packet instanceof PacketRegisterRequest) {
                                register(conn, (PacketRegisterRequest) packet);
                            }
                            if (packet instanceof PacketQueueJoin) {
                                PacketQueueJoin joinQueuePacket = (PacketQueueJoin) packet;
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
    public void onPacket(Packet packet, InetAddress address) {
        if (packet instanceof PacketPing) {
            UDPConnection.getInstance().sendPacket(new PacketPong().setID(((PacketPing) packet).getID()), address);
        }
    }
}
