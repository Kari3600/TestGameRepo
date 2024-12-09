package com.Kari3600.me.TestGameCommon.Champions;

import java.util.HashMap;

import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.MovingEntity;
import com.Kari3600.me.TestGameCommon.Player;

public abstract class Champion extends MovingEntity {
    private Player player;
    protected HashMap<Ability.Key,Ability> abilities = new HashMap<Ability.Key,Ability>();

    protected abstract void setUpAbilities();

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public Ability getAbility(Ability.Key key) {
        return abilities.get(key);
    }

    public void onDeath(Entity killer) {
        // TODO implement champ death
    }

    public Champion(GameEngine ge) {
        super(ge);
        setUpAbilities();
    }
}
