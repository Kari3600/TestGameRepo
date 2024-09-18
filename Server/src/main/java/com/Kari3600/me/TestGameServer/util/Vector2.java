package com.Kari3600.me.TestGameServer.util;

public class Vector2 {
    public float x;
    public float y;

    public Vector2 add(float x, float y) {
        return new Vector2(this.x+x, this.y+y);
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(this.x+v.x, this.y+v.y);
    }

    public Vector2 substract(Vector2 v) {
        return new Vector2(this.x-v.x, this.y-v.y);
    }

    public Vector2 multiply(float v) {
        return new Vector2(this.x*v, this.y*v);
    }

    public Vector2 normalize() {
        double div = Math.sqrt(x*x+y*y);
        return new Vector2(x/div,y/div);
    }

    public Vector2 invertY() {
        return new Vector2(x,-y);
    }

    public Vector3 to3D() {
        return new Vector3(x,0,y);
    }

    public Vector2 clone() {
        return new Vector2(x,y);
    }

    @Override
    public String toString() {
        return "Vector2("+x+", "+y+")";
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(double x, double y) {
        this.x = (float)x;
        this.y = (float)y;
    }

    public Vector2() {
        x=0;
        y=0;
    }
}
