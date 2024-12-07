package com.Kari3600.me.TestGameServer;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimerTask;

import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.LivingEntity;
import com.Kari3600.me.TestGameCommon.Path;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.packets.TCPConnection;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;
import com.Kari3600.me.TestGameCommon.packets.PacketEntityAdd;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public class GameEngineServer extends TimerTask implements GameEngine {

    private final float TPS = 20;
    private final Set<Entity> entities = new HashSet<Entity>();
    private final Map<LivingEntity,HashSet<Entity>> visibleEntities = new HashMap<>();
    private final Map<TCPConnection,Champion> championMap = new HashMap<>();
    private final Map<Champion,TCPConnection> connectionMap = new HashMap<>();
    private long tick = 0;
    private long lastTick = 0;

    public void addEntity(Entity entity) {
        entities.add(entity);
        if (entity instanceof LivingEntity) {
            visibleEntities.put((LivingEntity) entity, new HashSet<>());
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
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
            if (entity instanceof LivingEntity) {
                for (Entity e : entities) {
                    boolean visible = false;
                    if (entity.getPosition().substract(e.getPosition()).length() < 500) {
                        visible = true;
                    }
                    Set<Entity> vEntities = visibleEntities.get((LivingEntity) entity);
                    if (visible && !vEntities.contains(entity)) {
                        vEntities.add(e);
                        if (entity instanceof Champion) {
                            UDPConnection.getInstance().sendPacket(new PacketEntityAdd().setPosition(e.getPosition()).setEntityID(e.getID()).setTick(tick),connectionMap.get(entity).getHostAddress());
                        }
                    }
                    
                }
            }
        }
        tick++;
        //System.out.println(String.format("New player position: %f, %f, %f",player.getPosition().x,player.getPosition().y,player.getPosition().z));
    }

}
