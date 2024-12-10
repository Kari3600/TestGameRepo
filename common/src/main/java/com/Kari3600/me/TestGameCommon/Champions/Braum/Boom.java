package com.Kari3600.me.TestGameCommon.Champions.Braum;

import com.Kari3600.me.TestGameCommon.Element;
import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.LivingEntity;
import com.Kari3600.me.TestGameCommon.ProjectileRunnable;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.Champions.Projectile;

public class Boom extends Projectile {

    @Override
    protected int collisionRadius() {
        return 3000;
    }

    @Override
    protected int speed() {
        return 30000;
    }

    @Override
    protected int maxDistance() {
        return 30000;
    }

    @Override
    protected boolean piercing() {
        return false;
    }

    @Override
    protected ProjectileRunnable onHit() {
        return new ProjectileRunnable() {
            @Override
            public void run(Entity entity) {
                System.out.println("onHit");
                if (entity instanceof LivingEntity) {
                    ((LivingEntity)entity).damage(300, Element.STONE, getChampion());
                }
            }
        };
    }

    @Override
    protected long cooldown() {
        return 3000;
    }

    public Boom(Champion champion) {
        super(champion);
    }

}
