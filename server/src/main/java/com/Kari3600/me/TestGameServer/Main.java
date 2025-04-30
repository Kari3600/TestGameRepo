package com.Kari3600.me.TestGameServer;

import com.Kari3600.me.TestGameCommon.GameState;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;

public class Main {

    private static GameState gameState;

    public static GameState getGameEngine() {
        return gameState;
    }

    public static void printMatrix(float[] matrix) {
        for (int x=0;x<4;x++) {
            System.out.println(matrix[0+x*4]+" | "+matrix[1+x*4]+" | "+matrix[2+x*4]+" | "+matrix[3+x*4]);
        }
    }

    public static void main(String[] args) {
        
        //gameState = new GameState();
        //gameState.setPlayerCharacter(new Braum());
        //gameState.registerEntity(new Braum());
        //new Timer().scheduleAtFixedRate(gameState, 1000L/20, 1000L/20);
        ServerSocketManager ssm = new ServerSocketManager();
        new UDPConnection(2138).register(ssm);
        new Thread(ssm).start();
        
        System.out.println("Hello World");
    }
}
