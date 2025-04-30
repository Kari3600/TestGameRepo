package com.Kari3600.me.TestGameCommon.game;

import com.Kari3600.me.TestGameCommon.packets.Packet;



public class GameResync implements Packet {
    private final long tick;
    private final long time;

    public long getTick() {
        return tick;
    }

    public long getTime() {
        return time;
    }

    public GameResync(long tick, long time) {
        this.tick = tick;
        this.time = time;
    }
}
