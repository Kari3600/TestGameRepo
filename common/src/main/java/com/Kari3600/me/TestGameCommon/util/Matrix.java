package com.Kari3600.me.TestGameCommon.util;

public class Matrix {

    private float[] m = new float[] {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};

    public Vector3 getPosition() {
        return new Vector3(m[12],m[13],m[14]);
    }

    public void setPosition(Vector3 position) {
        m[12] = position.x;
        m[13] = position.y;
        m[14] = position.z;
    }

    public void setRotation(float angle) {
        m[0] = (float)Math.cos(angle);
        m[2] = (float)Math.sin(angle);
        m[8] = (float)-Math.sin(angle);
        m[10] = (float)Math.cos(angle);
    }

    public void lookVector(Vector3 v) {
        Vector3 vn = v.normalize();
        m[2] = vn.x;
        m[10] = vn.z;
        m[0] = -vn.z;
        m[8] = vn.x;
    }

    public float[] getValues() {
        return m;
    }

}
