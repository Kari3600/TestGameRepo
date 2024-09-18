package com.Kari3600.me.TestGameCommon.Champions;

import com.Kari3600.me.TestGameCommon.ProjectileEntity;
import com.Kari3600.me.TestGameCommon.ProjectileRunnable;
import com.Kari3600.me.TestGameCommon.util.Object3D;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Projectile extends Castable {

    private Object3D model;
    private int collisionRadius;
    private int speed;
    private int maxDistance;
    private boolean piercing;
    private ProjectileRunnable onHit;
    
    @Override
    protected void onCast(Vector3 location) {
        new ProjectileEntity(getChampion(),model.clone(), collisionRadius, speed, maxDistance, getChampion().getPosition(), location.substract(getChampion().getPosition()).normalize(), piercing, onHit);
    }

    public Projectile(Champion champion, long cooldown, Object3D model, int collisionRadius, int speed, int maxDistance, boolean piercing, ProjectileRunnable onHit) {
        super(champion,cooldown);
        this.model = model;
        this.collisionRadius = collisionRadius;
        this.speed = speed;
        this.maxDistance = maxDistance;
        this.onHit = onHit;
    }

}
