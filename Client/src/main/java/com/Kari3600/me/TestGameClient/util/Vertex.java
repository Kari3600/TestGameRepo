package com.Kari3600.me.TestGameClient.util;

import com.Kari3600.me.TestGameCommon.util.Vector3;

public class Vertex {
    private Vector3 position;
    private double[] textureCoord;

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public double[] getTextureCoord() {
        return textureCoord;
    }

    public Vertex(Vector3 position) {
        this.position = position;
    }
    public Vertex(Vector3 position, double textureCoordX, double textureCoordY) {
        this.position = position;
        this.textureCoord = new double[] {textureCoordX,textureCoordY};
    }

}
