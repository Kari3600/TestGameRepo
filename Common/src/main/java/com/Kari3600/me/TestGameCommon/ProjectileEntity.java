package com.Kari3600.me.TestGameCommon;

import java.util.HashSet;
import java.util.Set;

import com.Kari3600.me.TestGameCommon.util.Vector3;

public class ProjectileEntity extends Entity {

    private int speed;
    private int maxDistance;
    private int distance = 0;
    private Vector3 direction;
    private boolean piercing;
    private ProjectileRunnable onHit;

    private Set<Entity> entitiesHit = new HashSet<Entity>();

    @Override
    protected int collisionRadius() {
        return 0;
    }

    @Override
    public void perTick(float TPS) {
        if (distance<maxDistance) {
            setPosition(getPosition().add(direction.multiply(speed/TPS)));
            distance+=speed/TPS;
            Set<Entity> entities = checkCollision();
            for (Entity entity : entities) {
                if (entitiesHit.contains(entity)) continue;
                onHit.run(entity);
                entitiesHit.add(entity);
                if (!piercing) {
                    remove();
                    return;
                }
            }
        } else {
            remove();
        }
    }

    public ProjectileEntity(GameEngine ge, Entity caster, int speed, int maxDistance, Vector3 position, Vector3 direction, boolean piercing, int collisionRadius, ProjectileRunnable onHit) {
        super(ge);
        setPosition(position);
        this.speed = speed;
        this.maxDistance = maxDistance;
        this.direction = direction;
        this.piercing = piercing;
        setCollisionRadius(collisionRadius);
        this.onHit = onHit;
        entitiesHit.add(caster);
    }
    
}
