package com.Kari3600.me.TestGameCommon.events.entity;


import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.GameState;
import com.Kari3600.me.TestGameCommon.events.GameEvent;
import com.Kari3600.me.TestGameCommon.events.LocalizedEvent;
import com.Kari3600.me.TestGameCommon.util.Vector3;

import java.util.UUID;


public abstract class EntityEvent extends GameEvent implements LocalizedEvent {
    protected final UUID entityId;
    private final Vector3 position;

    public Vector3 getPosition() {
        return position;
    }

    public Entity getEntity() {
        return GameState.getCurrentGameState().getEntity(entityId);
    }

    public EntityEvent(long tick, Entity entity) {
        super(tick);
        this.entityId = entity.getID();
        this.position = entity.getPosition();
    }
}
