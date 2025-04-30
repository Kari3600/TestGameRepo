package com.Kari3600.me.TestGameCommon;

import java.lang.reflect.Constructor;
import java.util.*;

import com.Kari3600.me.RapidSerializer.TreeSerializable;
import com.Kari3600.me.TestGameCommon.util.Matrix;
import com.Kari3600.me.TestGameCommon.util.Vector3;

@TreeSerializable
public abstract class Entity {

    public enum Team{RED,NEUTRAL,BLUE}

    private final UUID id;
    private Matrix matrix = new Matrix();
    private int collisionRadius;
    private Team team;

    public UUID getID() {
        return id;
    }

    public int getCollisionRadius() {
        return collisionRadius;
    }
    public void setCollisionRadius(int collisionRadius) {
        this.collisionRadius = collisionRadius;
    }

    public Set<Entity> checkCollision() {
        Set<Entity> set = new HashSet<Entity>();
        for (Entity entity : GameState.getCurrentGameState().getAllEntities()) {
            if (entity == this) continue;
            if (entity.getPosition().substract(getPosition()).length() <= entity.getCollisionRadius()+getCollisionRadius()) {
                set.add(entity);
            }
        }
        return set;
    }

    public Vector3 getPosition() {
        return matrix.getPosition();
    }

    public void setPosition(Vector3 position) {
        matrix.setPosition(position);
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public abstract void perTick();

    public void remove() {
        GameState.getCurrentGameState().removeEntity(this);
    }

    public Entity(UUID id, int collisionRadius) {
        if (id == null) id = UUID.randomUUID();
        this.id = id;
        this.collisionRadius = collisionRadius;
        GameState.getCurrentGameState().addEntity(this);
    }

}
