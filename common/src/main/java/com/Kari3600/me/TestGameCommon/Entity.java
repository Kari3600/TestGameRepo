package com.Kari3600.me.TestGameCommon;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.Kari3600.me.TestGameCommon.util.Matrix;
import com.Kari3600.me.TestGameCommon.util.Transposing;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Entity implements Transposing {

    public enum Team{RED,NEUTRAL,BLUE}

    private final UUID id;
    private Matrix matrix = new Matrix();
    private int collisionRadius = 0;
    private Team team;

    public UUID getID() {
        return id;
    }

    protected abstract int collisionRadius();
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

    public abstract void perTick(float TPS, GameState gs);

    public void remove() {
        GameState.getCurrentGameState().removeEntity(this);
    }

    public Entity(UUID id) {
        if (id == null) id = UUID.randomUUID();
        this.id = id;
        this.collisionRadius = collisionRadius();
        GameState.getCurrentGameState().addEntity(this);
    }

}
