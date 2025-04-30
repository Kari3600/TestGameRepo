package com.Kari3600.me.TestGameCommon;

import java.util.UUID;

public abstract class LivingEntity extends Entity {

    private int maxHealth;
    private int health;

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

    public LivingEntity(UUID id, int collisionRadius, int maxHealth) {
        super(id,collisionRadius);
        this.maxHealth = maxHealth;
    }
}
