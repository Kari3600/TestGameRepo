package com.Kari3600.me.TestGameCommon.Champions;

import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Castable extends Ability {

    private long toNextCast = 0;
    private long cooldown;

    protected abstract long cooldown();
    protected abstract void onCast(Vector3 location);

    @Override
    public boolean onPress(Vector3 location) {
        if (System.currentTimeMillis() >= toNextCast) {
            onCast(location);
            toNextCast = System.currentTimeMillis()+cooldown;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onRelease(Vector3 location) {
        return true;
    }

    public Castable(Champion champion) {
        super(champion);
        this.cooldown = cooldown();
    }

}
