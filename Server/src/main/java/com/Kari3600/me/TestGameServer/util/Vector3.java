package com.Kari3600.me.TestGameServer.util;

public class Vector3 {
    public float x;
    public float y;
    public float z;

    public Vector3 add(float x, float y, float z) {
        return new Vector3(this.x+x, this.y+y, this.z+z);
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(this.x+v.x, this.y+v.y, this.z+v.z);
    }

    public Vector3 substract(Vector3 v) {
        return new Vector3(this.x-v.x, this.y-v.y, this.z-v.z);
    }

    public Vector3 multiply(float v) {
        return new Vector3(this.x*v, this.y*v, this.z*v);
    }

    public double length() {
        return Math.sqrt(x*x+y*y+z*z);
    }

    public Vector3 normalize() {
        double div = this.length();
        return new Vector3(x/div,y/div,z/div);
    }

    public Vector3 lerp(Vector3 target, float units) {
        return add(target.substract(this).normalize().multiply(units));
    }

    public Vector2 to2D() {
        return new Vector2(x,z);
    }

    public Vector3 clone() {
        return new Vector3(x,y,z);
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(double x, double y, double z) {
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
    }

    public Vector3() {
        x=0;
        y=0;
        z=0;
    }
}
