package com.Kari3600.me.TestGameCommon.Champions;

import com.Kari3600.me.TestGameCommon.util.Vector3;

import java.util.HashMap;

public abstract class Ability {

    public static HashMap<Key,Ability> buildAbilities(Ability abilityQ, Ability abilityW, Ability abilityE, Ability abilityR) {
        HashMap<Key,Ability> abilities = new HashMap<Key,Ability>();
        abilities.put(Key.Q, abilityQ);
        abilities.put(Key.W, abilityW);
        abilities.put(Key.E, abilityE);
        abilities.put(Key.R, abilityR);
        return abilities;
    }

    public enum Key{Q,W,E,R}

    public abstract boolean onPress(Champion champion, Vector3 location);

    public abstract boolean onRelease(Champion champion, Vector3 location);

    public Ability() {}
}
