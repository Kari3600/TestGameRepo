package com.Kari3600.me.TestGameClient.util;

import java.util.HashSet;

import com.Kari3600.me.TestGameClient.Main;
import com.Kari3600.me.TestGameCommon.util.Vector3;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLRunnable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Object3D {
    public static Object3D getTetrahedron() {
        Vertex A = new Vertex(new Vector3(-1,-1,-1));
        Vertex B = new Vertex(new Vector3(1,-1,1));
        Vertex C = new Vertex(new Vector3(-1,1,1));
        Vertex D = new Vertex(new Vector3(1,1,-1));
        HashSet<Triangle> testTriangles = new HashSet<Triangle>();
        testTriangles.add(new Triangle(A, B, C,new float[]{1f,0f,0f}));
        testTriangles.add(new Triangle(A, B, D,new float[]{0f,1f,0f}));
        testTriangles.add(new Triangle(A, C, D,new float[]{0f,0f,1f}));
        testTriangles.add(new Triangle(B, C, D,new float[]{1f,1f,1f}));
        return new Object3D(testTriangles);
    }

    private final HashSet<Triangle> triangles;
    private Texture texture;
    private final TextureInputStream textureIS;
    private float[] Matrix = new float[] {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
    

    public Object3D movePivot(Vector3 position) {
        HashSet<Vertex> vertices = new HashSet<Vertex>();
        for (Triangle triangle : triangles) {
            for (Vertex vertex : triangle.getVertices()) {
                vertices.add(vertex);
            }
        }
        for (Vertex vertex : vertices) {
            vertex.setPosition(vertex.getPosition().add(position));
        }
        return this;
    }

    public HashSet<Triangle> getTriangles() {
        return triangles;
    }

    public void loadTexture() {
        if (textureIS == null) return;
        if (texture != null) return;
        Main.getGameRenderer().getCanvas().invoke(false, new GLRunnable() {
            @Override
            public boolean run(GLAutoDrawable drawable) {
                try {
                    texture = TextureIO.newTexture(textureIS,true,"test");
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    public void unloadTexture() {
        
    }

    public Texture getTexture() {
        return texture;
    }

    public void render(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glMultMatrixf(Matrix,0);
        if (texture != null) {
            texture.enable(gl);
            texture.bind(gl);
        }
        gl.glBegin(GL2.GL_TRIANGLES);
        for (Triangle tri : triangles) {
            float[] color = tri.getColor();
            gl.glColor3f(color[0],color[1],color[2]);
            //gl.gltext
            for (Vertex v : tri.getVertices()) {
                Vector3 v3 = v.getPosition();
                if (texture != null) {
                    double[] t = v.getTextureCoord();
                    gl.glTexCoord2d(t[0],t[1]);
                }
                gl.glVertex3f(v3.x, v3.y, v3.z);
            }
        }
        gl.glEnd();
    }

    public Object3D clone() {
        Object3D clone = new Object3D(triangles);
        clone.texture = this.texture;
        return clone;
    }

    public Object3D(HashSet<Triangle> triangles) {
        this.textureIS = null;
        this.triangles = triangles;
    }

    public Object3D(HashSet<Triangle> triangles, TextureInputStream textureIS) {
        this.textureIS = textureIS;
        this.triangles = triangles;
    }
}
