package com.Kari3600.me.TestGameClient;

import java.util.Set;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.packets.PacketEntity;
import com.Kari3600.me.TestGameCommon.packets.PacketEntityAdd;
import com.Kari3600.me.TestGameCommon.packets.PacketGame;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;
import com.Kari3600.me.TestGameCommon.packets.UDPPacketListener;
import com.Kari3600.me.TestGameCommon.packets.UDPSingleConnection;
import com.Kari3600.me.TestGameCommon.util.Vector3;
import com.jogamp.opengl.util.texture.spi.awt.IIOTextureProvider;
import com.Kari3600.me.TestGameCommon.Entity;
import com.Kari3600.me.TestGameCommon.GameEngine;
import com.Kari3600.me.TestGameCommon.Path;

public class GameEngineClient extends TimerTask implements GameEngine, UDPPacketListener {

    private final UDPSingleConnection connection;
    private final ConcurrentHashMap<Long,ConcurrentLinkedQueue<Packet>> buffer = new ConcurrentHashMap<>();

    public UDPSingleConnection getConnection() {
        return connection;
    }

    private final float TPS = 20;
    private Champion character;
    private final Map<UUID,Entity> entities = new HashMap<>();
    private long tick = 0;
    private long lastTick = 0;

    public void addEntity(Entity entity) {
        entities.put(entity.getID(),entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity.getID());
    }

    public Champion getPlayerCharacter() {
        return character;
    }

    public void setPlayerCharacter(Champion character) {
        this.character = character;
    }

    public Collection<Entity> getAllEntities() {
        return entities.values();
    }

    @Override
    public void run() {
        if (tick%100 == 0) {
            long tTime = System.currentTimeMillis();
            System.out.println(100F*1000/(tTime-lastTick));
            lastTick = tTime;
        }
        //System.out.println("Tick");
        for (Entity entity : entities.values().toArray(new Entity[]{})) {
            entity.perTick(TPS);
        }
        tick++;
        //System.out.println(String.format("New player position: %f, %f, %f",player.getPosition().x,player.getPosition().y,player.getPosition().z));
    }

    @Override
    public void onPacket(Packet packet, InetAddress address) {
        if (!(packet instanceof PacketGame)) return;
        if (packet instanceof PacketEntity) {
            Entity entity = null;
            if (packet instanceof PacketEntityAdd) {
                PacketEntityAdd packetadd = (PacketEntityAdd) packet;
                try {
                    entity = (Entity) Class.forName(packetadd.getClassName()).getConstructor(GameEngine.class,UUID.class).newInstance(this,packetadd.getEntityID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                entity = entities.get(((PacketEntity) packet).getEntityID());
            }
            if (entity == null) {
                throw new IllegalStateException("Entity does not exist");
            }
        }
    }

    public GameEngineClient(InetAddress host) {
        this.connection = new UDPSingleConnection(host);
        int delta90 = (int) connection.checkConnection();
        int bufferSize = (int) (delta90*TPS/1000)+1;
        System.out.println("Buffer size: "+bufferSize);
    }

}
