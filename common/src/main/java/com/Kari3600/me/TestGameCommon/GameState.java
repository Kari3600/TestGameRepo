package com.Kari3600.me.TestGameCommon;

import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.packets.PacketEntity;
import com.Kari3600.me.TestGameCommon.packets.PacketEntityAdd;
import com.Kari3600.me.TestGameCommon.packets.PacketGame;

import java.io.*;
import java.util.*;

public class GameState implements Serializable {
    public static final float TPS = 20;

    private static GameState gameState;
    public static GameState getCurrentGameState() {
        return gameState;
    }
    public static void setCurrentGameState(GameState gameState) {
        GameState.gameState = gameState;
    }
    protected final Map<UUID,Entity> entities = new HashMap<UUID,Entity>();
    protected final List<PacketGame> events = new ArrayList<>();
    public void addEntity(Entity e) {
        entities.put(e.getID(),e);
    }
    public void removeEntity(Entity e) {
        entities.remove(e.getID());
    }
    public Collection<Entity> getAllEntities() {
        return entities.values();
    }
    public void addEvent(PacketGame e) {
        events.add(e);
    }
    public void tick() {
        //TODO divide events and entity ticks by priority
        for (PacketGame event : events) {
            processPacket(event);
        }
        for (Entity e : entities.values()) {
            e.perTick();
        }
    }
    private void processPacket(Packet packet) {
        if (!(packet instanceof PacketGame)) return;
        if (packet instanceof PacketEntity) {
            Entity entity = null;
            if (packet instanceof PacketEntityAdd) {
                PacketEntityAdd packetadd = (PacketEntityAdd) packet;
                try {
                    entity = (Entity) Class.forName(packetadd.getClassName()).getConstructor(GameState.class,UUID.class).newInstance(this,packetadd.getEntityID());
                    System.out.println("Added entity "+packetadd.getClassName());
                    //TODO implement model rendering client-side
//                    Object3D model = ModelLoader.getObject3D(packetadd.getClassName());
//                    model.setTransposer(entity);
//                    Main.getGameRenderer().addObject(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                entity = entities.get(((PacketEntity) packet).getEntityID());
            }
            if (entity == null) {
                throw new IllegalStateException("Entity does not exist");
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
}
