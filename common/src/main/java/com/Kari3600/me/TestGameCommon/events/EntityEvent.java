package com.Kari3600.me.TestGameCommon.events;

import com.Kari3600.me.TestGameAnnotations.packets.PacketEvent;

import java.util.UUID;

@PacketEvent
public class EntityEvent extends GameEvent {
    protected final UUID entityId;

    public EntityEvent(long tick, UUID entityId) {
        super(tick);
        this.entityId = entityId;
    }
}
