package com.Kari3600.me.TestGameCommon.Champions;

import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Ability {
    public enum Key{Q,W,E,R}
    private Champion champion;

    public Champion getChampion() {
        return champion;
    }

    public abstract boolean onPress(Vector3 location);

    public abstract boolean onRelease(Vector3 location);

    public Ability(Champion champion) {
        this.champion = champion;
    }
}
