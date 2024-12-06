package com.Kari3600.me.TestGameServer;

import java.net.ServerSocket;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.Kari3600.me.TestGameCommon.packets.Connection;
import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginRequest;
//import com.Kari3600.me.TestGameCommon.packets.QueueJoinPacket;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginResponse;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginResult;
import com.Kari3600.me.TestGameCommon.packets.PacketLoginTask;
import com.Kari3600.me.TestGameCommon.packets.PacketQueueJoin;
import com.Kari3600.me.TestGameCommon.packets.PacketRegisterRequest;
import com.Kari3600.me.TestGameCommon.packets.PacketRegisterResult;
import com.Kari3600.me.TestGameCommon.util.EncryptionUtil;

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
                        if (packet instanceof PacketLoginRequest) {
                            String salt = EncryptionUtil.encrypt(String.valueOf(System.currentTimeMillis()));
                            CompletableFuture<Packet> packetFuture = CompletableFuture.supplyAsync(() -> {
                                try {
                                    return conn.sendPacketRequest(new PacketLoginTask().setSalt(salt)).get();
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            });
                            CompletableFuture<ResultSet> databaseFuture = DatabaseManager.executeQuery("SELECT `Password` FROM `Users` WHERE `Username` = ?",((PacketLoginRequest)packet).getUsername());
                            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(packetFuture, databaseFuture);

                            combinedFuture.thenRun(() -> {
                                try {
                                    Packet returnPacket = packetFuture.get();
                                    ResultSet rs = databaseFuture.get();
                                    if (!(returnPacket instanceof PacketLoginResponse)) {
                                        throw new Exception("Wrong packet");
                                    }
                                    PacketLoginResponse response = (PacketLoginResponse) returnPacket;
                                    if (!rs.next()) {
                                        conn.sendPacket(new PacketLoginResult().setStatus((byte) 1));
                                        return;
                                    }
                                    if (!response.getPassword().equals(EncryptionUtil.encrypt(salt+rs.getString("Password")))) {
                                        conn.sendPacket(new PacketLoginResult().setStatus((byte) 2));
                                        return;
                                    }
                                    conn.sendPacket(new PacketLoginResult().setStatus((byte) 0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                
                            });
                        }
                        if (packet instanceof PacketRegisterRequest) {
                            CompletableFuture<Integer> databaseFuture = DatabaseManager.executeUpdate("INSERT IGNORE INTO users (username, password) VALUES (?, ?)",((PacketRegisterRequest)packet).getUsername(),((PacketRegisterRequest)packet).getPassword());

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
                        if (packet instanceof PacketQueueJoin) {
                            PacketQueueJoin joinQueuePacket = (PacketQueueJoin) packet;
                            System.out.println("Player joined");
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
