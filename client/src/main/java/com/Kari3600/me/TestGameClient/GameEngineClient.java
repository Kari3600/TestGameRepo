package com.Kari3600.me.TestGameClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.GameState;

import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.events.GameEvent;
import com.Kari3600.me.TestGameCommon.events.ServerBoundEvent;
import com.Kari3600.me.TestGameCommon.game.GameResync;
import com.Kari3600.me.TestGameCommon.packets.UDPSingleConnection;

public class GameEngineClient extends GameEngine {
    public static GameEngineClient getInstance() {
        return (GameEngineClient) instance;
    }

    private final UDPSingleConnection connection;
    protected final ConcurrentHashMap<Long, List<GameEvent>> buffer = new ConcurrentHashMap<>();
    protected int bufferSize;
    private Champion character;

    public UDPSingleConnection getConnection() {
        return connection;
    }

    public Champion getPlayerCharacter() {
        return character;
    }

    public void setPlayerCharacter(Champion character) {
        this.character = character;
    }

    public void sendPacket(Packet packet) {
        connection.sendPacket(packet);
    }

    public GameEngineClient(InetAddress host) {
        instance = this;
        GameState.setCurrentGameState(new GameState(-100000));
        this.connection = new UDPSingleConnection(new InetSocketAddress(host, 2138));
        connection.register(this);
        int delta90 = (int) connection.checkConnection();
        bufferSize = (int) (delta90*GameState.TPS/1000)+2;
        System.out.println("Buffer size: "+bufferSize);
        for (int x=-bufferSize;x<=0;++x) {
            buffer.put((long)x, new ArrayList<>());
        }
    }

    @Override
    public void run() {
        long tick = GameState.getCurrentGameState().getTick();
        List<GameEvent> events = buffer.remove(tick);
        tick++;
        buffer.put(tick+bufferSize,new ArrayList<>());
        GameState.getCurrentGameState().tick(events);
    }

    @Override
    public void fireEvent(GameEvent event) {
        if (event instanceof ServerBoundEvent) return;
        buffer.get(event.getTick()).add(event);
    }

    @Override
    public void onPacket(Packet packet, InetSocketAddress address) {
        if (packet instanceof GameResync) {
            System.out.println("Resynchronizing game");
            GameResync resync = (GameResync) packet;
            long currServerTick = (long) (resync.getTick() + (System.currentTimeMillis()-resync.getTime())*GameState.TPS/1000);
            GameState.setCurrentGameState(new GameState(currServerTick-bufferSize));
            buffer.clear();
            for (long x=currServerTick-bufferSize;x<=currServerTick;++x) {
                buffer.put(x, new ArrayList<>());
            }
        }
        if (!(packet instanceof GameEvent)) return;
        GameEvent event = (GameEvent) packet;
        long eventTick = event.getTick();
        long currTick = GameState.getCurrentGameState().getTick();
        if (eventTick < currTick) {
            // TODO resync simulation
            System.out.println("Outdated packet, resynchronizing");
            return;
        }
        if (eventTick > currTick+bufferSize) {
            // TODO buffer overflow
            System.out.println("Buffer overflow, resynchronizing (tick: "+currTick+", max buffer tick: "+(currTick+bufferSize)+", event tick: "+eventTick+")");
            return;
        }
        buffer.get(eventTick).add(event);
    }
}
