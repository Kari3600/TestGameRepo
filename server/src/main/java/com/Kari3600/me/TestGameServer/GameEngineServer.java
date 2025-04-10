package com.Kari3600.me.TestGameServer;

import java.util.Set;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;

import com.Kari3600.me.TestGameCommon.*;
import com.Kari3600.me.TestGameCommon.GameState;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;
import com.Kari3600.me.TestGameCommon.packets.UDPPacketListener;
import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.packets.PacketEntityAdd;
import com.Kari3600.me.TestGameCommon.packets.PacketMoveCommand;

public class GameEngineServer extends TimerTask implements UDPPacketListener {

    private final Map<LivingEntity,HashSet<Entity>> visibleEntities = new HashMap<>();
    private final Map<InetAddress,Player> playerMap = new HashMap<>();
    private long tick = 0;
    private long lastTick = 0;

    @Override
    public void run() {
        if (tick%100 == 0) {
            long tTime = System.currentTimeMillis();
            System.out.println(100F*1000/(tTime-lastTick));
            lastTick = tTime;
        }
        //System.out.println("Tick");
        for (Entity entity : entities.toArray(new Entity[]{})) {
            entity.perTick();
            if (entity instanceof LivingEntity) {
                for (Entity e : entities) {
                    boolean visible = false;
                    if (entity.getPosition().substract(e.getPosition()).length() < 500) {
                        visible = true;
                    }
                    Set<Entity> vEntities = visibleEntities.get((LivingEntity) entity);
                    if (visible && !vEntities.contains(entity)) {
                        vEntities.add(e);
                        if (entity instanceof Champion) {
                            InetAddress address = ((Champion) entity).getPlayer().getAddress();
                            PacketEntityAdd packet = (PacketEntityAdd) new PacketEntityAdd().setClassName(e.getClass().getName()).setPosition(e.getPosition()).setEntityID(e.getID()).setTick(tick);
                            System.out.println("Spawning "+e.getClass().getSimpleName()+" for player "+((Champion) entity).getPlayer().getUsername());
                            UDPConnection.getInstance().sendPacket(packet,address);
                        }
                    }
                    
                }
            }
        }
        tick++;
        //System.out.println(String.format("New player position: %f, %f, %f",player.getPosition().x,player.getPosition().y,player.getPosition().z));
    }

    public void onPacket(Packet packet, InetAddress address) {
        Player player = playerMap.get(address);
        if (player == null) return;
        if (packet instanceof PacketMoveCommand) {
            System.out.println(player.getUsername()+" clicked at location "+((PacketMoveCommand) packet).getLocation());
            player.getControllingEntity().setPath(new Path(player.getControllingEntity(), ((PacketMoveCommand) packet).getLocation()));
        }
    }

    public GameEngineServer(Set<Player> players) {
        UDPConnection.getInstance().register(this);
        for (Player player : players) {
            playerMap.put(player.getAddress(),player);
            try {
                Champion playerChamp = player.getChampion().getConstructor(GameState.class, UUID.class).newInstance(this,null);
                playerChamp.setPlayer(player);
                player.setControllingEntity(playerChamp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
