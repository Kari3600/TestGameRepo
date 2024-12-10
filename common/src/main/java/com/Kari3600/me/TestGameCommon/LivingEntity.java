package com.Kari3600.me.TestGameCommon;

import java.util.UUID;

public abstract class LivingEntity extends Entity {

    private int maxHealth;
    private int health;

    protected abstract int maxHealth();
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getHealth() {
        return health;
    }

    public void damage(int value, Element element, Entity source) {
        this.health-=value;
    }

    public abstract void onDeath(Entity killer);

    public LivingEntity(GameEngine ge, UUID id) {
        super(ge,id);
        this.maxHealth = maxHealth();
    }
}
