package com.Kari3600.me.TestGameServer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.Kari3600.me.TestGameCommon.packets.Connection;
import com.Kari3600.me.TestGameCommon.packets.PacketQueueCount;
import com.Kari3600.me.TestGameCommon.packets.PacketQueueStart;

public class Queue {
    private static byte maxPlayers = 2;
    private static LinkedList<Connection> playersQueue = new LinkedList<Connection>();
    private static Queue currentQueue = new Queue();

    public static synchronized void queuePlayer(Connection player) {
        if (currentQueue.add(player)) {
            currentQueue.start();
            currentQueue = new Queue();
        }
    }


    Set<Connection> players = new HashSet<Connection>();

    private void start() {
        for (Connection pl : players) {
            pl.sendPacket(new PacketQueueStart());
        }
    }

    private boolean add(Connection player) {
        players.add(player);
        for (Connection pl : players) {
            pl.sendPacket(new PacketQueueCount().setCount((byte) players.size()));
        }
        if (players.size() == maxPlayers) return true;
        return false;
    }
}