package com.Kari3600.me.TestGameCommon.Champions.Braum;

import com.Kari3600.me.TestGameCommon.ModelLoader;
import com.Kari3600.me.TestGameCommon.Champions.Ability;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public class Braum extends Champion {

    @Override
    protected void setUpAbilities() {
        abilities.put(Ability.Key.Q,new Boom(this));
    }

    public Braum() {
        super(ModelLoader.getObject3DfromGLB("Braum",0).movePivot(new Vector3(-4500,0,-2000)),5000,2500,3000);
    }
    
}
