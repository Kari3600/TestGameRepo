package com.Kari3600.me.TestGameCommon.Champions.Braum;

import com.Kari3600.me.TestGameCommon.Element;
import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.LivingEntity;
import com.Kari3600.me.TestGameCommon.ModelLoader;
import com.Kari3600.me.TestGameCommon.ProjectileRunnable;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.Champions.Projectile;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public class Boom extends Projectile {

    public Boom(Champion champion) {
        super(champion,3000,ModelLoader.getObject3DfromGLB("Braum",1).movePivot(new Vector3(-4500,0,-2000)),1000,30000,30000,false,new ProjectileRunnable() {
            @Override
            public void run(Entity entity) {
                System.out.println("onHit");
                if (entity instanceof LivingEntity) {
                    ((LivingEntity)entity).damage(300, Element.STONE, champion);
                }
            }
            
        });
    }

}
