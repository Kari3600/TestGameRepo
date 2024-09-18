package com.Kari3600.me.TestGameCommon;

import com.Kari3600.me.TestGameCommon.util.Object3D;

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

    public LivingEntity(Object3D model, int collisionRadius, int basicMaxHealth) {
        super(model,collisionRadius);
        this.maxHealth = basicMaxHealth;
        this.health = basicMaxHealth;
    }

    public LivingEntity() {
        super();
    }
}
