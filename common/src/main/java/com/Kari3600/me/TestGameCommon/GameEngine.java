package com.Kari3600.me.TestGameCommon;

import com.Kari3600.me.TestGameCommon.events.GameEvent;
import com.Kari3600.me.TestGameCommon.packets.PacketListener;
import com.Kari3600.me.TestGameCommon.util.Vector3;

import java.util.TimerTask;

public abstract class GameEngine extends TimerTask implements PacketListener {
    protected static GameEngine instance;

    public static GameEngine getInstance() {
        if (instance == null) {
            throw new RuntimeException("GameEngineServer not initialized");
        }
        return instance;
    }

    protected boolean canSee(Entity entity, Vector3 location) {
        // TODO implement view range
        return entity.getPosition().substract(location).length() <= 400;
    }

    public abstract void fireEvent(GameEvent event);
}
