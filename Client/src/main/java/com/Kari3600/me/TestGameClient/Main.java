package com.Kari3600.me.TestGameClient;

import java.util.Properties;
import java.util.Timer;
import java.util.UUID;

import javax.swing.SwingUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.Kari3600.me.TestGameClient.gui.ClientPane;
import com.Kari3600.me.TestGameClient.gui.LoginPanel;
import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.Champions.Braum.Braum;
import com.Kari3600.me.TestGameCommon.packets.TCPConnection;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;
import com.Kari3600.me.TestGameCommon.packets.PacketManager;

public class Main {

    private static TCPConnection conn;
    private static GameEngineClient gameEngine;
    private static GameRenderer gameRenderer;
    private static ClientPane clientRenderer;
    private static UUID playerID = UUID.randomUUID();

    public static GameEngineClient getGameEngine() {
        return gameEngine;
    }

    public static GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    public static UUID getPlayerID() {
        return playerID;
    }

    public static TCPConnection getConnection() {
        return conn;
    }

    public static void printMatrix(float[] matrix) {
        for (int x=0;x<4;x++) {
            System.out.println(matrix[0+x*4]+" | "+matrix[1+x*4]+" | "+matrix[2+x*4]+" | "+matrix[3+x*4]);
        }
    }

    public static void launchGame() {
        gameEngine = new GameEngineClient();
        gameRenderer = new GameRenderer();
        new Timer().scheduleAtFixedRate(gameEngine, 1000L/20, 1000L/20);
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("client.config")) {
            properties.load(fis);
        } catch (FileNotFoundException ex) {
            System.out.println("Missing 'client.config' file.");
        } catch (IOException ex) {
            System.out.println("Failed to load configuration.");
        }

        try {
            conn = new TCPConnection(InetAddress.getByName(properties.getProperty("host", "localhost")));
        } catch (Exception e) {
            // TODO try to reconnect
            System.out.println("Failed to connect to the server.");
            conn = null;
        }

        new UDPConnection().checkConnection(conn.getHostAddress());

        SwingUtilities.invokeLater(LoginPanel::new);

        //System.out.println(Path.fromMapCoordinates(Path.toMapCoordinated(new Vector2(4.0F,3.0F))).toString());
        
        System.out.println("Hello World");
    }
}
