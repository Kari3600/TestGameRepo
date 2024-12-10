package com.Kari3600.me.TestGameServer;

import java.util.Timer;

import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.Champions.Braum.Braum;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;

public class Main {

    private static GameEngine gameEngine;

    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    public static void printMatrix(float[] matrix) {
        for (int x=0;x<4;x++) {
            System.out.println(matrix[0+x*4]+" | "+matrix[1+x*4]+" | "+matrix[2+x*4]+" | "+matrix[3+x*4]);
        }
    }

    public static void main(String[] args) {
        
        //gameEngine = new GameEngine();
        //gameEngine.setPlayerCharacter(new Braum());
        //gameEngine.registerEntity(new Braum());
        //new Timer().scheduleAtFixedRate(gameEngine, 1000L/20, 1000L/20);
        ServerSocketManager ssm = new ServerSocketManager();
        new UDPConnection().register(ssm);
        new Thread(ssm).start();
        
        System.out.println("Hello World");
    }
}
