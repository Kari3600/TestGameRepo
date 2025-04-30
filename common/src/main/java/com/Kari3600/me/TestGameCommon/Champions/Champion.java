package com.Kari3600.me.TestGameCommon.Champions;

import java.util.HashMap;
import java.util.UUID;

import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.GameState;
import com.Kari3600.me.TestGameCommon.MovingEntity;
import com.Kari3600.me.TestGameCommon.Player;

public abstract class Champion extends MovingEntity {
    private Player player;
    protected AbilitySet abilities;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public AbilitySet getAbilities() {
        return abilities;
    }

    public void onDeath(Entity killer) {
        // TODO implement champ death
    }

    public Champion(UUID id, int collisionRadius, int maxHealth, float moveSpeed, AbilitySet abilities) {
        super(id,collisionRadius,maxHealth,moveSpeed);
        this.abilities = abilities;
    }
}
