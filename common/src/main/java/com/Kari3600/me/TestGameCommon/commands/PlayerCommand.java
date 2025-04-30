package com.Kari3600.me.TestGameCommon.commands;

import com.Kari3600.me.TestGameCommon.Player;
import com.Kari3600.me.TestGameCommon.packets.Packet;

public abstract class PlayerCommand implements Packet {
    public abstract void process(Player player);
}
