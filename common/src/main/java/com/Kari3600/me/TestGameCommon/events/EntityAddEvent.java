package com.Kari3600.me.TestGameCommon.events;

import com.Kari3600.me.TestGameAnnotations.packets.PacketEvent;

import java.util.UUID;

@PacketEvent
public class EntityAddEvent extends EntityEvent {
    public EntityAddEvent(long tick, UUID entityId) {
        super(tick, entityId);
    }
}
