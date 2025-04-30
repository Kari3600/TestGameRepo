package com.Kari3600.me.TestGameCommon.events.entity;


import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.events.ServerBoundEvent;
import com.Kari3600.me.TestGameCommon.util.Vector3;

import java.util.UUID;


public class EntityAddEvent extends EntityEvent implements ServerBoundEvent {
    private final Entity entity;

    @Override
    public Entity getEntity() {
        return entity;
    }

    public EntityAddEvent(long tick, Entity entity) {
        super(tick, entity);
        this.entity = entity;
    }
}
