package com.Kari3600.me.TestGameClient;

import java.util.Timer;

import com.Kari3600.me.TestGameCommon.Champions.Braum.Braum;

public class Main {

    private static GameEngine gameEngine;
    private static GameRenderer gameRenderer;

    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    public static GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    public static void printMatrix(float[] matrix) {
        for (int x=0;x<4;x++) {
            System.out.println(matrix[0+x*4]+" | "+matrix[1+x*4]+" | "+matrix[2+x*4]+" | "+matrix[3+x*4]);
        }
    }

    public static void main(String[] args) {
        
        new ClientSocketManager().tryToConnect();
        //gameEngine = new GameEngine();
        //gameRenderer = new GameRenderer();
        //System.out.println(Path.fromMapCoordinates(Path.toMapCoordinated(new Vector2(4.0F,3.0F))).toString());
        //gameEngine.setPlayerCharacter(new Braum());
        //gameEngine.registerEntity(new Braum());
        //new Timer().scheduleAtFixedRate(gameEngine, 1000L/20, 1000L/20);
        
        System.out.println("Hello World");
    }
}
