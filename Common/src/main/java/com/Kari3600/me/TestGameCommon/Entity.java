package com.Kari3600.me.TestGameCommon;

import java.util.HashSet;
import java.util.Set;

import com.Kari3600.me.TestGameCommon.util.Object3D;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Entity {

    public enum Team{RED,NEUTRAL,BLUE}

    private Object3D model;
    private int collisionRadius = 0;
    private Team team;

    public Object3D getModel() {
        return model;
    }

    public void setModel(Object3D model) {
        this.model = model;
        Main.getGameRenderer().addObject(model);
    }

    public int getCollisionRadius() {
        return collisionRadius;
    }

    public void setCollisionRadius(int collisionRadius) {
        this.collisionRadius = collisionRadius;
    }

    public Set<Entity> checkCollision() {
        Set<Entity> set = new HashSet<Entity>();
        for (Entity entity : Main.getGameEngine().getAllEntities()) {
            if (entity == this) continue;
            if (entity.getPosition().substract(getPosition()).length() <= entity.getCollisionRadius()+getCollisionRadius()) {
                set.add(entity);
            }
        }
        return set;
    }

    public Vector3 getPosition() {
        return model.getPosition().clone();
    }

    public void setPosition(Vector3 position) {
        model.setPosition(position);
    }

    public abstract void perTick(float TPS);

    public void remove() {
        Main.getGameEngine().unregisterEntity(this);
        if (model != null) {
            Main.getGameRenderer().removeObject(model);
        }
    }

    public Entity() {
        Main.getGameEngine().registerEntity(this);
    }

    public Entity(Object3D model,int collisionRadius) {
        this.model = model;
        this.collisionRadius = collisionRadius;
        Main.getGameEngine().registerEntity(this);
        Main.getGameRenderer().addObject(model);
    }

}
