package com.Kari3600.me.TestGameCommon;

import com.Kari3600.me.TestGameCommon.util.Object3D;
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
    public void perTick(float TPS) {
        if (path == null) return;
        Vector3[] res = path.progress(getPosition(), moveSpeed/TPS);
        if (res == null) {
            path = null;
            return;
        }
        getModel().setPosition(res[0]);
        getModel().lookVector(res[1]);
    }

    public MovingEntity() {
        super();
    }

    public MovingEntity(Object3D model, Vector3 position, int collisionRadius, int basicMaxHealth) {
        super(model,collisionRadius,basicMaxHealth);
        model.setPosition(position);
    }
}
