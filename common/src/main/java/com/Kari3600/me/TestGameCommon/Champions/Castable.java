package com.Kari3600.me.TestGameCommon.Champions;

import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Castable extends Ability {

    private long toNextCast = 0;
    private long cooldown;

    protected abstract void onCast(Champion champion, Vector3 location);

    @Override
    public boolean onPress(Champion champion, Vector3 location) {
        if (System.currentTimeMillis() >= toNextCast) {
            onCast(champion, location);
            toNextCast = System.currentTimeMillis()+cooldown;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onRelease(Champion champion, Vector3 location) {
        return true;
    }

    public Castable(long cooldown) {
        this.cooldown = cooldown;
    }

}
