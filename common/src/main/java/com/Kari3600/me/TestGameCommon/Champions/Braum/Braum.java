package com.Kari3600.me.TestGameCommon.Champions.Braum;

import java.util.UUID;

import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.Champions.Ability;
import com.Kari3600.me.TestGameCommon.Champions.Champion;

public class Braum extends Champion {

    @Override
    protected void setUpAbilities() {
        abilities.put(Ability.Key.Q,new Boom(this));
    }

    @Override
    protected int moveSpeed() {
        return 5000;
    }

    @Override
    protected int maxHealth() {
        return 3000;
    }

    @Override
    protected int collisionRadius() {
        return 2500;
    }

    public Braum(GameEngine ge, UUID id) {
        super(ge,id);
    }
    
}
