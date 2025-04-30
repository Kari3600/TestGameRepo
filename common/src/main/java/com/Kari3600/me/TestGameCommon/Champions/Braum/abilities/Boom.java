package com.Kari3600.me.TestGameCommon.Champions.Braum.abilities;

import com.Kari3600.me.TestGameCommon.*;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.Champions.Projectile;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public class Boom extends Projectile {
    @Override
    protected ProjectileEntity getProjectileEntity(Champion champion, Vector3 position, Vector3 direction) {
        return new BoomRocket(champion, position, direction);
    }

    public Boom() {
        super(3000);
    }

}
