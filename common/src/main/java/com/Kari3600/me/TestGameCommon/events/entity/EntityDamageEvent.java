package com.Kari3600.me.TestGameCommon.events.entity;


import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.events.ServerBoundEvent;
import com.Kari3600.me.TestGameCommon.util.Vector3;

import java.io.Serializable;
import java.util.UUID;


public class EntityDamageEvent extends EntityEvent implements ServerBoundEvent {
    public EntityDamageEvent(long tick, Entity entity) {
        super(tick,entity);
    }
}
