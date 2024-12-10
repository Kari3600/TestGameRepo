package com.Kari3600.me.TestGameAnnotations.packets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface CreatePackets {
    CreatePacket[] value();
}
