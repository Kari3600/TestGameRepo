package com.Kari3600.me.TestGameClient.util;

import com.jogamp.opengl.GLAutoDrawable;

public abstract class Object2D {
    public abstract void preRender(GLAutoDrawable drawable);
    public abstract void render(GLAutoDrawable drawable);
}
