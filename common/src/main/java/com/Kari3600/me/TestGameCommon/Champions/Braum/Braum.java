package com.Kari3600.me.TestGameCommon.Champions.Braum;

import java.util.UUID;

import com.Kari3600.me.TestGameCommon.Champions.AbilitySet;
import com.Kari3600.me.TestGameCommon.Champions.Braum.abilities.Boom;
import com.Kari3600.me.TestGameCommon.Champions.Ability;
import com.Kari3600.me.TestGameCommon.Champions.Champion;

public class Braum extends Champion {
    public Braum(UUID id) {
        super(id, 2500, 3000, 5000, new AbilitySet(new Boom(),new Boom(),new Boom(),new Boom()));
    }
    
}
