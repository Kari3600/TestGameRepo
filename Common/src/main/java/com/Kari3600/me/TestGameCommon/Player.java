package com.Kari3600.me.TestGameCommon;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.packets.TCPConnection;

public class Player {

    private static Map<InetAddress,Player> players = new HashMap<>();

    public static Player getByAddress(InetAddress address) {
        return players.get(address);
    }

    private final String username;
    private InetAddress address;
    private TCPConnection connection;
    private Class<? extends Champion> champion;
    private MovingEntity controllingEntity;

    public String getUsername() {
        return username;
    }

    public InetAddress getAddress() {
        return address;
    }

    public TCPConnection getConnection() {
        return connection;
    }

    public Class<? extends Champion> getChampion() {
        return champion;
    }

    public void setChampion(Class<? extends Champion> champion) {
        this.champion = champion;
    }

    public MovingEntity getControllingEntity() {
        return controllingEntity;
    }

    public void setControllingEntity(MovingEntity controllingEntity) {
        this.controllingEntity = controllingEntity;
    }

    public Player(String username, TCPConnection connection) {
        this.username = username;
        this.connection = connection;
        this.address = connection.getHostAddress();
        players.put(address, this);
        // TODO doubled players
    }
}
