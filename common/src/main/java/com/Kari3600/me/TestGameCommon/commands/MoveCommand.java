package com.Kari3600.me.TestGameCommon.commands;


import com.Kari3600.me.TestGameCommon.Path;
import com.Kari3600.me.TestGameCommon.Player;
import com.Kari3600.me.TestGameCommon.util.Vector3;


public class MoveCommand extends PlayerCommand {
    private final Vector3 position;

    public Vector3 getPosition() {
        return position;
    }

    @Override
    public void process(Player player) {
        player.getControllingEntity().setPath(new Path(player.getControllingEntity(), position));
    }

    public MoveCommand(Vector3 position) {
        this.position = position;
    }
}
