package com.Kari3600.me.TestGameClient;

import java.net.InetAddress;
import java.util.Collection;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.Kari3600.me.TestGameCommon.GameState;

import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.packets.PacketEntity;
import com.Kari3600.me.TestGameCommon.packets.PacketEntityAdd;
import com.Kari3600.me.TestGameCommon.packets.PacketGame;
import com.Kari3600.me.TestGameCommon.packets.UDPPacketListener;
import com.Kari3600.me.TestGameCommon.packets.UDPSingleConnection;
import com.Kari3600.me.TestGameClient.util.Object3D;
import com.Kari3600.me.TestGameCommon.Entity;

public class GameEngineClient extends TimerTask implements UDPPacketListener {

    private final UDPSingleConnection connection;
    private final ConcurrentHashMap<Long,byte[]> buffer = new ConcurrentHashMap<>();
    private int bufferSize;
    private Champion character;
    private long tick = 0;

    public UDPSingleConnection getConnection() {
        return connection;
    }

    public Champion getPlayerCharacter() {
        return character;
    }

    public void setPlayerCharacter(Champion character) {
        this.character = character;
    }

    @Override
    public void run() {
        //System.out.println("Tick");
        if (tick<0) return;
        buffer.put(tick++,GameState.getCurrentGameState().serialize());
        buffer.remove(tick-bufferSize);
        GameState.getCurrentGameState().tick();
        //System.out.println(String.format("New player position: %f, %f, %f",player.getPosition().x,player.getPosition().y,player.getPosition().z));
    }

    @Override
    public void onPacket(Packet packet, InetAddress address) {
        if (!(packet instanceof PacketGame)) return;
        PacketGame packetGame = (PacketGame) packet;
        long tickID = packetGame.getTick();
        if (tickID > tick) {
            //TODO better error catching
            System.err.println("Simulation delayed.");
            return;
        }
        byte[] serializedGameState = buffer.get(tickID);
        if (serializedGameState == null) {
            //TODO resync simulation
            System.err.println("Outdated packet.");
            return;
        }
        GameState gameState = GameState.deserialize(serializedGameState);
        gameState.addEvent(packetGame);
        GameState.setCurrentGameState(gameState);
        for (long subTick = tickID; subTick <= tick; subTick++) {
            gameState.tick();
            buffer.put(subTick, gameState.serialize());
        }
    }

    public GameEngineClient(InetAddress host) {
        this.connection = new UDPSingleConnection(host);
        connection.register(this);
        int delta90 = (int) connection.checkConnection();
        bufferSize = (int) (delta90*GameState.TPS/1000)+1;
        System.out.println("Buffer size: "+bufferSize);
        tick = -bufferSize/2;
        //TODO init buffer
        for (int x=0;x<bufferSize;++x) {
            buffer.put((long)x, new ConcurrentLinkedQueue<>());
        }
    }

}
