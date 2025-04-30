package com.Kari3600.me.TestGameCommon.Champions.Braum.abilities;

import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.Element;
import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.LivingEntity;
import com.Kari3600.me.TestGameCommon.ProjectileEntity;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public class BoomRocket extends ProjectileEntity {

    @Override
    public void onHit(Entity caster, Entity entity) {
        System.out.println("onHit");
        ((LivingEntity)entity).damage(300, Element.STONE, caster);
    }

    public BoomRocket(Champion champion, Vector3 position, Vector3 direction) {
        super(null, champion, 300, 500, position, direction, true, 300);
    }
}
