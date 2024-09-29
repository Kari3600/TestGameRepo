package com.Kari3600.me.TestGameCommon.Champions;

import com.Kari3600.me.TestGameCommon.ProjectileEntity;
import com.Kari3600.me.TestGameCommon.ProjectileRunnable;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Projectile extends Castable {

    private int collisionRadius;
    private int speed;
    private int maxDistance;
    private boolean piercing;
    private ProjectileRunnable onHit;

    protected abstract int collisionRadius();
    protected abstract int speed();
    protected abstract int maxDistance();
    protected abstract boolean piercing();
    protected abstract ProjectileRunnable onHit();

    
    @Override
    protected void onCast(Vector3 location) {
        new ProjectileEntity(getChampion().getEngine() ,getChampion(), speed, maxDistance, getChampion().getPosition(), location.substract(getChampion().getPosition()).normalize(), piercing, collisionRadius, onHit);
    }

    public Projectile(Champion champion) {
        super(champion);
        this.collisionRadius = collisionRadius();
        this.speed = speed();
        this.maxDistance = maxDistance();
        this.piercing = piercing();
        this.onHit = onHit();
    }

}
