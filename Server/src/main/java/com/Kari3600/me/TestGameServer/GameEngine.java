package com.Kari3600.me.TestGameServer;

import java.util.Set;
import java.util.HashSet;
import java.util.TimerTask;

import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.Path;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public class GameEngine extends TimerTask {

    private final float TPS = 20;
    private Champion character;
    private final Set<Entity> entities = new HashSet<Entity>();
    private long tick = 0;
    private long lastTick = 0;

    public void registerEntity(Entity entity) {
        entities.add(entity);
    }

    public void unregisterEntity(Entity entity) {
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

    public synchronized void createPlayerPath(Vector3 destination) {
        character.setPath(new Path(character, destination));
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

}
