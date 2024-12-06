package com.Kari3600.me.TestGameCommon.packets;

import com.Kari3600.me.TestGameCommon.util.Vector2;

public class EntityPathPacket {
    private Vector2 destination;

    public Vector2 getDestination() {
        return destination;
    }
    public void setDestination(Vector2 destination) {
        this.destination = destination;
    }
}
