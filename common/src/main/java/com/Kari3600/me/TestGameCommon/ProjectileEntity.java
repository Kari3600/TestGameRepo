package com.Kari3600.me.TestGameCommon;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class ProjectileEntity extends Entity {

    private Entity caster;
    private int speed;
    private int maxDistance;
    private int distance = 0;
    private Vector3 direction;
    private boolean piercing;

    private Set<Entity> entitiesHit = new HashSet<Entity>();

    protected abstract void onHit(Entity caster, Entity e);

    @Override
    public void perTick() {
        if (distance>maxDistance) {
            remove();
            return;
        }
        setPosition(getPosition().add(direction.multiply(speed/GameState.TPS)));
        distance+=speed/GameState.TPS;
        Set<Entity> entities = checkCollision();
        for (Entity entity : entities) {
            if (entitiesHit.contains(entity)) continue;
            if (!(entity instanceof LivingEntity)) continue;
            onHit(caster, entity);
            entitiesHit.add(entity);
            if (!piercing) {
                remove();
                return;
            }
        }
    }

    public ProjectileEntity(UUID id, Entity caster, int speed, int maxDistance, Vector3 position, Vector3 direction, boolean piercing, int collisionRadius) {
        super(id,collisionRadius);
        setPosition(position);
        this.speed = speed;
        this.maxDistance = maxDistance;
        this.direction = direction;
        this.piercing = piercing;
        entitiesHit.add(caster);
    }
    
}
