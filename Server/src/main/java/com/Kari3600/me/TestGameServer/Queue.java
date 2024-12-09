package com.Kari3600.me.TestGameServer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;

import com.Kari3600.me.TestGameCommon.packets.TCPConnection;
import com.Kari3600.me.TestGameCommon.Player;
import com.Kari3600.me.TestGameCommon.packets.PacketQueueCount;
import com.Kari3600.me.TestGameCommon.packets.PacketQueueStart;

public class Queue {
    private static final byte maxPlayers = 1;
    private static LinkedList<Player> playersQueue = new LinkedList<Player>();
    private static Queue currentQueue = new Queue();

    public static synchronized void queuePlayer(Player player) {
        if (currentQueue.add(player)) {
            currentQueue.start();
            currentQueue = new Queue();
        }
    }


    Set<Player> players = new HashSet<>();

    private void start() {
        for (Player pl : players) {
            pl.getConnection().sendPacket(new PacketQueueStart());
        }
        GameEngineServer gameEngine = new GameEngineServer(players);
        new Timer().scheduleAtFixedRate(gameEngine, 1000L/20, 1000L/20);
    }

    private boolean add(Player player) {
        players.add(player);
        for (Player pl : players) {
            pl.getConnection().sendPacket(new PacketQueueCount().setCount((byte) players.size()));
        }
        if (players.size() == maxPlayers) return true;
        return false;
    }
}
