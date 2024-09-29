package com.Kari3600.me.TestGameClient;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.Path;
import com.Kari3600.me.TestGameCommon.util.Vector2;
import com.Kari3600.me.TestGameCommon.util.Vector3;

public class MouseHandler implements MouseListener {

    private final GameEngineClient ge = Main.getGameEngine();
    private final GameRenderer gr;

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gr.toWorldLocation(new Vector2(e.getX(),e.getY()), new MouseRunnable() {
            @Override
            public void run(Vector3 location) {
                ge.getPlayerCharacter().setPath(new Path(ge.getPlayerCharacter(),location));
            }
        });
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        System.out.println("Unimplemented method 'mouseExited'");
    }

    public MouseHandler(GameRenderer gr) {
        this.gr = gr;
    }
}
