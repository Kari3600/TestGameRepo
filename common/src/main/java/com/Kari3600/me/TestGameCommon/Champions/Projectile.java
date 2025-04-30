package com.Kari3600.me.TestGameCommon.Champions;

import com.Kari3600.me.TestGameCommon.ProjectileEntity;
import com.Kari3600.me.TestGameCommon.ProjectileRunnable;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Projectile extends Castable {

    protected abstract ProjectileEntity getProjectileEntity(Champion champion, Vector3 position, Vector3 direction);
    
    @Override
    protected void onCast(Champion champion, Vector3 location) {
        getProjectileEntity(champion, champion.getPosition(), location.substract(champion.getPosition()).normalize());
    }

    public Projectile(long cooldown) {
        super(cooldown);
    }

}
