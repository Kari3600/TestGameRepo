package com.Kari3600.me.TestGameCommon;

import com.Kari3600.me.TestGameCommon.events.entity.EntityAddEvent;
import com.Kari3600.me.TestGameCommon.events.entity.EntityEvent;
import com.Kari3600.me.TestGameCommon.events.GameEvent;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameState implements Serializable {
    public static final float TPS = 20;

    private static GameState gameState;
    public static GameState getCurrentGameState() {
        return gameState;
    }
    public static void setCurrentGameState(GameState gameState) {
        GameState.gameState = gameState;
    }

    private long tick;
    protected final Map<UUID,Entity> entities = new HashMap<UUID,Entity>();
    public void addEntity(Entity e) {
        entities.put(e.getID(),e);
    }
    public void removeEntity(Entity e) {
        entities.remove(e.getID());
    }
    public Entity getEntity(UUID id) {
        return entities.get(id);
    }
    public Collection<Entity> getAllEntities() {
        return entities.values();
    }
    public void tick(List<GameEvent> events) {
        if (tick < 0) {
            tick++;
            return;
        }
        //TODO divide events and entity ticks by priority
        for (GameEvent event : events) {
            processEvent(event);
        }
        for (Entity e : entities.values()) {
            e.perTick();
        }
        tick++;
    }
    public long getTick() {
        return tick;
    }
    private void processEvent(GameEvent event) {
        if (event instanceof EntityEvent) {
            if (event instanceof EntityAddEvent) {
                EntityAddEvent entityAddEvent = (EntityAddEvent) event;
                addEntity(entityAddEvent.getEntity());
            } else {
                //TODO implement
            }
        }
    }
    public byte[] serialize() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }
    public static GameState deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (GameState) ois.readObject();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    public GameState(long tick) {
        this.tick = tick;
    }
}
