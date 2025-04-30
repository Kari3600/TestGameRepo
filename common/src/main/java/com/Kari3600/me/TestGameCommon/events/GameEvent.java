package com.Kari3600.me.TestGameCommon.events;

import com.Kari3600.me.TestGameCommon.packets.Packet;


import java.io.Serializable;


public abstract class GameEvent {
    protected final long tick;

    public long getTick() {
        return tick;
    }

    public GameEvent(long tick) {
        this.tick = tick;
    }
}
