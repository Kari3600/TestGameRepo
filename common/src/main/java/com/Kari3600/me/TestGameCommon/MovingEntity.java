package com.Kari3600.me.TestGameCommon;

import java.util.UUID;

import com.Kari3600.me.TestGameCommon.util.Vector3;

public abstract class MovingEntity extends LivingEntity {
    private Path path;
    private float moveSpeed;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setMoveSpeed(int speed) {
        this.moveSpeed = speed;
    }

    @Override
    public void perTick() {
        if (path == null) return;
        Vector3[] res = path.progress(getPosition(), moveSpeed/GameState.TPS);
        if (res == null) {
            path = null;
            return;
        }
        getMatrix().setPosition(res[0]);
        getMatrix().lookVector(res[1]);
    }

    public MovingEntity(UUID id, int collisionRadius, int maxHealth, float moveSpeed) {
        super(id,collisionRadius,maxHealth);
        this.moveSpeed = moveSpeed;
    }
}
