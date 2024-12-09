package com.Kari3600.me.TestGameClient;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import javax.swing.SwingUtilities;

import com.Kari3600.me.TestGameCommon.Champions.Ability;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.util.Vector2;

public class KeyHandler implements KeyListener {

    private final GameEngineClient ge = Main.getGameEngine();
    private final GameRenderer gr;
    private final HashSet<Integer> pressedKeys = new HashSet<Integer>();

    @Override
    public void keyPressed(KeyEvent event) {
        if (pressedKeys.contains(event.getKeyCode())) return;
        pressedKeys.add(event.getKeyCode());
        Champion champ = ge.getPlayerCharacter();
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation,Main.getGameRenderer().getFrame());
        gr.toWorldLocation( new Vector2(mouseLocation.getX(),mouseLocation.getY())).thenAcceptAsync(location -> {
            switch (event.getKeyChar()) {
                case 'q':
                champ.getAbility(Ability.Key.Q).onPress(location);
                break;
    
                case 'w':
                champ.getAbility(Ability.Key.W).onPress(location);
                break;
    
                case 'e':
                champ.getAbility(Ability.Key.E).onPress(location);
                break;
    
                case 'r':
                champ.getAbility(Ability.Key.R).onPress(location);
                break;
            }
        });
    }

    @Override
    public void keyReleased(KeyEvent event) {
        pressedKeys.remove(event.getKeyCode());
        Champion champ = ge.getPlayerCharacter();
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation,Main.getGameRenderer().getFrame());
        gr.toWorldLocation(new Vector2(mouseLocation.getX(),mouseLocation.getY())).thenAcceptAsync(location -> {
            switch (event.getKeyChar()) {
                case 'q':
                champ.getAbility(Ability.Key.Q).onRelease(location);
                break;
    
                case 'w':
                champ.getAbility(Ability.Key.W).onRelease(location);
                break;
    
                case 'e':
                champ.getAbility(Ability.Key.E).onRelease(location);
                break;
    
                case 'r':
                champ.getAbility(Ability.Key.R).onRelease(location);
                break;
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent event) {
        //System.out.println("Key typed");
    }

    public KeyHandler(GameRenderer gr) {
        this.gr = gr;
    }
}
