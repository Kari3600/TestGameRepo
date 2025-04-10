package com.Kari3600.me.TestGameCommon.events;

import com.Kari3600.me.TestGameAnnotations.packets.Packet;
import com.Kari3600.me.TestGameAnnotations.packets.PacketEvent;

@PacketEvent
public class GlobalEvent extends GameEvent implements Packet {

    public GlobalEvent(long tick) {
        super(tick);
    }
}
