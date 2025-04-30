package com.Kari3600.me.TestGameCommon.Champions;

import com.Kari3600.me.RapidSerializer.PrimitiveSerializable;

@PrimitiveSerializable
public class AbilitySet {
    private Ability abilityQ;
    private Ability abilityW;
    private Ability abilityE;
    private Ability abilityR;

    public Ability get(Ability.Key key) {
        switch (key) {
            case Q:
                return abilityQ;
            case W:
                return abilityW;
            case E:
                return abilityE;
            case R:
                return abilityR;
        }
        throw new IllegalArgumentException("Unknown ability key: " + key);
    }
    public AbilitySet(Ability abilityQ, Ability abilityW, Ability abilityE, Ability abilityR) {
        this.abilityQ = abilityQ;
        this.abilityW = abilityW;
        this.abilityE = abilityE;
        this.abilityR = abilityR;
    }
}
