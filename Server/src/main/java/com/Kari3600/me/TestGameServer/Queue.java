package com.Kari3600.me.TestGameServer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;

import com.Kari3600.me.TestGameCommon.packets.TCPConnection;
import com.Kari3600.me.TestGameCommon.packets.PacketQueueCount;
import com.Kari3600.me.TestGameCommon.packets.PacketQueueStart;

public class Queue {
    private static byte maxPlayers = 2;
    private static LinkedList<TCPConnection> playersQueue = new LinkedList<TCPConnection>();
    private static Queue currentQueue = new Queue();

    public static synchronized void queuePlayer(TCPConnection player) {
        if (currentQueue.add(player)) {
            currentQueue.start();
            currentQueue = new Queue();
        }
    }


    Set<TCPConnection> players = new HashSet<TCPConnection>();

    private void start() {
        for (TCPConnection pl : players) {
            pl.sendPacket(new PacketQueueStart());
        }
        GameEngineServer gameEngine = new GameEngineServer();
        new Timer().scheduleAtFixedRate(gameEngine, 1000L/20, 1000L/20);
    }

    private boolean add(TCPConnection player) {
        players.add(player);
        for (TCPConnection pl : players) {
            pl.sendPacket(new PacketQueueCount().setCount((byte) players.size()));
        }
        if (players.size() == maxPlayers) return true;
        return false;
    }
}
