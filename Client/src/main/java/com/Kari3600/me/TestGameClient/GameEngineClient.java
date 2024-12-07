package com.Kari3600.me.TestGameClient;

import java.util.Set;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;
import com.Kari3600.me.TestGameCommon.util.Vector3;
import com.jogamp.opengl.util.texture.spi.awt.IIOTextureProvider;
import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.Path;

public class GameEngineClient extends TimerTask implements GameEngine {

    private final ConcurrentHashMap<Long,ConcurrentLinkedQueue<Packet>> buffer = new ConcurrentHashMap<>();

    private final float TPS = 20;
    private Champion character;
    private final Set<Entity> entities = new HashSet<Entity>();
    private long tick = 0;
    private long lastTick = 0;

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public Champion getPlayerCharacter() {
        return character;
    }

    public void setPlayerCharacter(Champion character) {
        this.character = character;
    }

    public Set<Entity> getAllEntities() {
        return entities;
    }

    @Override
    public void run() {
        if (tick%100 == 0) {
            long tTime = System.currentTimeMillis();
            System.out.println(100F*1000/(tTime-lastTick));
            lastTick = tTime;
        }
        //System.out.println("Tick");
        for (Entity entity : entities.toArray(new Entity[]{})) {
            entity.perTick(TPS);
        }
        tick++;
        //System.out.println(String.format("New player position: %f, %f, %f",player.getPosition().x,player.getPosition().y,player.getPosition().z));
    }

    public GameEngineClient(InetAddress host) {
        int delta90 = (int) UDPConnection.getInstance().checkConnection(host);
        int bufferSize = (int) (delta90*TPS/1000)+1;
        System.out.println("Buffer size: "+bufferSize);
    }

}
