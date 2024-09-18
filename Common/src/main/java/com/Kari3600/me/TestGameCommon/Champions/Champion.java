package com.Kari3600.me.TestGameCommon.Champions;

import java.util.HashMap;

import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.Main;
import com.Kari3600.me.TestGameCommon.MovingEntity;
import com.Kari3600.me.TestGameCommon.util.EntityBar;
import com.Kari3600.me.TestGameCommon.util.Object3D;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class Champion extends MovingEntity {
    protected HashMap<Ability.Key,Ability> abilities = new HashMap<Ability.Key,Ability>();

    protected abstract void setUpAbilities();
    
    public Ability getAbility(Ability.Key key) {
        return abilities.get(key);
    }

    public void onDeath(Entity killer) {
        
    }

    public Champion(Object3D model, int baseMoveSpeed, int collisionRadius, int basicMaxHealth) {
        super(model,new Vector3(0,0,0),collisionRadius,basicMaxHealth);
        Main.getGameRenderer().addObject(new EntityBar(this));
        setMoveSpeed(baseMoveSpeed);
        setUpAbilities();
    }
}
