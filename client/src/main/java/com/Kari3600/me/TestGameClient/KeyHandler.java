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
                champ.getAbilities().get(Ability.Key.Q).onPress(champ,location);
                break;
    
                case 'w':
                champ.getAbilities().get(Ability.Key.W).onPress(champ,location);
                break;
    
                case 'e':
                champ.getAbilities().get(Ability.Key.E).onPress(champ,location);
                break;
    
                case 'r':
                champ.getAbilities().get(Ability.Key.R).onPress(champ,location);
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
                    champ.getAbilities().get(Ability.Key.Q).onRelease(champ,location);
                    break;

                case 'w':
                    champ.getAbilities().get(Ability.Key.W).onRelease(champ,location);
                    break;

                case 'e':
                    champ.getAbilities().get(Ability.Key.E).onRelease(champ,location);
                    break;

                case 'r':
                    champ.getAbilities().get(Ability.Key.R).onRelease(champ,location);
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
