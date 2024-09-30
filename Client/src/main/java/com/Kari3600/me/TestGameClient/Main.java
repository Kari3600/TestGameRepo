package com.Kari3600.me.TestGameClient;

import java.util.Timer;
import java.util.UUID;
import java.io.File;
import java.net.InetAddress;

import com.Kari3600.me.TestGameClient.gui.ClientPane;
import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.Champions.Braum.Braum;
import com.Kari3600.me.TestGameCommon.packets.Connection;
import com.Kari3600.me.TestGameCommon.packets.JoinQueuePacket;
import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.packets.QueueCountPacket;
import com.Kari3600.me.TestGameCommon.packets.StartQueuePacket;

public class Main {

    private static InetAddress host;
    private static GameEngineClient gameEngine;
    private static GameRenderer gameRenderer;
    private static ClientPane clientRenderer;
    private static UUID playerID = UUID.randomUUID();

    public static GameEngineClient getGameEngine() {
        return gameEngine;
    }

    public static GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    public static UUID getPlayerID() {
        return playerID;
    }

    public static void printMatrix(float[] matrix) {
        for (int x=0;x<4;x++) {
            System.out.println(matrix[0+x*4]+" | "+matrix[1+x*4]+" | "+matrix[2+x*4]+" | "+matrix[3+x*4]);
        }
    }

    public static void joinGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JoinQueuePacket packet = new JoinQueuePacket(playerID);
                Connection conn = new Connection(host);
                conn.sendPacket(packet);
                byte count = 0;
                while (true) {
                    Packet p = conn.waitForPacket();
                    if (p instanceof QueueCountPacket) {
                        QueueCountPacket queueCountPacket = (QueueCountPacket) p;
                        count = queueCountPacket.getCount();
                        clientRenderer.queueUpPlayers(count);
                        System.out.println("Current player count: "+count);
                    } else if (p instanceof StartQueuePacket) {
                        gameEngine = new GameEngineClient();
                        gameRenderer = new GameRenderer();
                        new Timer().scheduleAtFixedRate(gameEngine, 1000L/20, 1000L/20);
                    } else {
                        System.err.println("Wrong packet received");
                        break;
                    }
                    
                }
            }
        }).start();
    }

    public static void main(String[] args) {

        try {
            host = InetAddress.getLocalHost();
        } catch (Exception e) {
            host = null;
        }
        clientRenderer = new ClientPane();

        //System.out.println(Path.fromMapCoordinates(Path.toMapCoordinated(new Vector2(4.0F,3.0F))).toString());
        
        System.out.println("Hello World");
    }
}
