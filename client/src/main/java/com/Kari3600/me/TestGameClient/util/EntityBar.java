package com.Kari3600.me.TestGameClient.util;

import com.Kari3600.me.TestGameClient.GameRenderer;
import com.Kari3600.me.TestGameCommon.LivingEntity;
import com.Kari3600.me.TestGameClient.Main;
import com.Kari3600.me.TestGameCommon.util.Vector2;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;

public class EntityBar extends Object2D {

    private final GameRenderer gr = Main.getGameRenderer();

    public LivingEntity entity;
    public Vector2 position;

    @Override
    public void preRender(GLAutoDrawable drawable) {
        position = gr.toScreenLocation(drawable, entity.getPosition().add(0, 10000, 0));
    }

    @Override
    public void render(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        float healthPercentage = (float) entity.getHealth() / entity.getMaxHealth();
        String entityName = entity.toString();

        // Drawing the health bar
        float barWidth = 50.0f;  // Width of the health bar
        float barHeight = 5.0f;  // Height of the health bar
        float barX = position.x - barWidth / 2;
        float barY = position.y - barHeight / 2;  // Offset to place it above the player

        // Background of the health bar (gray)
        gl.glColor3f(0.5f, 0.5f, 0.5f);  // Gray
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(barX, barY);
        gl.glVertex2f(barX + barWidth, barY);
        gl.glVertex2f(barX + barWidth, barY + barHeight);
        gl.glVertex2f(barX, barY + barHeight);
        gl.glEnd();

        // Health bar foreground (green)
        gl.glColor3f(0.0f, 1.0f, 0.0f);  // Green
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(barX, barY);
        gl.glVertex2f(barX + (barWidth * healthPercentage), barY);
        gl.glVertex2f(barX + (barWidth * healthPercentage), barY + barHeight);
        gl.glVertex2f(barX, barY + barHeight);
        gl.glEnd();

        // Draw the player's name above the health bar
        TextRenderer textRenderer = gr.getTextRenderer();
        textRenderer.beginRendering((int) gr.getViewportSize().getWidth(),(int) gr.getViewportSize().getHeight());
        textRenderer.setColor(1.0f, 0.75f, 0.75f, 0.75f);  // Set text color (white)
        int textX = (int) (position.x - textRenderer.getBounds(entityName).getWidth() / 2);
        int textY = (int) (barY + barHeight + 10);  // Offset above the health bar
        textRenderer.draw(entityName, textX, textY);
        textRenderer.endRendering();
    }

    public EntityBar(LivingEntity entity) {
        this.entity = entity;
    }
}
