package com.Kari3600.me.TestGameAnnotations.packets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Repeatable(CreatePackets.class)
public @interface CreatePacket {
    String name();
    String parent();
    Field[] fields();

    @interface Field {
        Class<?> type();
        String name();
    }
    
}
