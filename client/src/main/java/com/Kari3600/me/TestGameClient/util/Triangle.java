package com.Kari3600.me.TestGameClient.util;

public class Triangle {
    private final Vertex[] vertices;
    private final float[] color;

    public Vertex[] getVertices() {
        return vertices;
    }

    public float[] getColor() {
        return color;
    }

    public Triangle(Vertex A, Vertex B, Vertex C, float[] color) {
        this.vertices = new Vertex[] {A,B,C};
        this.color = color;
    }
}
