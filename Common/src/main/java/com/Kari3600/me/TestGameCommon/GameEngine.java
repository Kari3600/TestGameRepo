package com.Kari3600.me.TestGameCommon;

import java.util.Set;

public interface GameEngine {

    public void addEntity(Entity entity);
    public void removeEntity(Entity entity);
    public Set<Entity> getAllEntities();
}
