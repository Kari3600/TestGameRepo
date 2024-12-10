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

    protected abstract int moveSpeed();
    public void setMoveSpeed(int speed) {
        this.moveSpeed = speed;
    }

    @Override
    public void perTick(float TPS) {
        if (path == null) return;
        Vector3[] res = path.progress(getPosition(), moveSpeed/TPS);
        if (res == null) {
            path = null;
            return;
        }
        getMatrix().setPosition(res[0]);
        getMatrix().lookVector(res[1]);
    }

    public MovingEntity(GameEngine ge, UUID id) {
        super(ge,id);
    }
}
