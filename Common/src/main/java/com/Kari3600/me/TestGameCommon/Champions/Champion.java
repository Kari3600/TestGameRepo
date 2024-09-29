package com.Kari3600.me.TestGameCommon.Champions;

import java.util.HashMap;

import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.MovingEntity;

public abstract class Champion extends MovingEntity {
    protected HashMap<Ability.Key,Ability> abilities = new HashMap<Ability.Key,Ability>();

    protected abstract void setUpAbilities();
    
    public Ability getAbility(Ability.Key key) {
        return abilities.get(key);
    }

    public void onDeath(Entity killer) {
        
    }

    public Champion(GameEngine ge) {
        super(ge);
        setUpAbilities();
    }
}
