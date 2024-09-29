package com.Kari3600.me.TestGameCommon;

import java.util.HashSet;
import java.util.Set;

import com.Kari3600.me.TestGameCommon.util.Matrix;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Entity {

    public enum Team{RED,NEUTRAL,BLUE}

    private GameEngine ge;
    private Matrix matrix = new Matrix();
    private int collisionRadius = 0;
    private Team team;

    protected abstract int collisionRadius();
    public int getCollisionRadius() {
        return collisionRadius;
    }
    public void setCollisionRadius(int collisionRadius) {
        this.collisionRadius = collisionRadius;
    }

    public Set<Entity> checkCollision() {
        Set<Entity> set = new HashSet<Entity>();
        for (Entity entity : ge.getAllEntities()) {
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

    public abstract void perTick(float TPS);

    public GameEngine getEngine() {
        return ge;
    }

    public void remove() {
        ge.removeEntity(this);
    }

    public Entity(GameEngine ge) {
        this.ge = ge;
        this.collisionRadius = collisionRadius();
        ge.addEntity(this);
    }

}
