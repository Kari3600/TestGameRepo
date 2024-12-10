package com.Kari3600.me.TestGameCommon;

import java.util.Collection;

public interface GameEngine {

    public void addEntity(Entity entity);
    public void removeEntity(Entity entity);
    public Collection<Entity> getAllEntities();
}
