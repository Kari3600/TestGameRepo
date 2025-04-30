package com.Kari3600.me.TestGameServer;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.Kari3600.me.TestGameCommon.packets.Packet;
import com.Kari3600.me.TestGameCommon.*;
import com.Kari3600.me.TestGameCommon.Champions.Champion;
import com.Kari3600.me.TestGameCommon.commands.PlayerCommand;
import com.Kari3600.me.TestGameCommon.events.GameEvent;
import com.Kari3600.me.TestGameCommon.events.LocalizedEvent;
import com.Kari3600.me.TestGameCommon.events.ServerBoundEvent;
import com.Kari3600.me.TestGameCommon.events.entity.EntityAddEvent;
import com.Kari3600.me.TestGameCommon.game.GameJoin;
import com.Kari3600.me.TestGameCommon.game.GameResync;
import com.Kari3600.me.TestGameCommon.packets.UDPConnection;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class GameEngineServer extends GameEngine {
    private final Multimap<LivingEntity,Entity> visibleEntities = HashMultimap.create();
    private List<GameEvent> events = new ArrayList<>();
    private final Object lock = new Object();
    private final Set<Player> players;
    private final Map<InetSocketAddress,Player> onlinePlayers = new HashMap<>();

    @Override
    public void run() {
        List<GameEvent> e;
        synchronized (lock) {
            e = events;
            events = new ArrayList<>();
        }
        for (Player player : onlinePlayers.values()) {
            for (Entity entity : GameState.getCurrentGameState().getAllEntities()) {
                boolean visible = canSee(player.getControllingEntity(),entity.getPosition());
                if (visible & !visibleEntities.containsEntry(player.getControllingEntity(),entity)) {
                    visibleEntities.put(player.getControllingEntity(),entity);
                    UDPConnection.getInstance().sendPacket(
                            new EntityAddEvent(GameState.getCurrentGameState().getTick(),entity),
                            player.getSocketAddress()
                    );
                }
            }
        }
        GameState.getCurrentGameState().tick(e);
    }

    @Override
    public void onPacket(Packet packet, InetSocketAddress address) {
        System.out.println("Received packet: " + packet.getClass().getSimpleName());
        if (packet instanceof GameJoin) {
            Player player = players.stream()
                    .filter(p -> p.getAddress().equals(address.getAddress()))
                    .findFirst()
                    .orElse(null);
            if (player == null) return;
            onlinePlayers.put(address,player);
            player.setSocketAddress(address);
            UDPConnection.getInstance().sendPacket(new GameResync(GameState.getCurrentGameState().getTick(), System.currentTimeMillis()), player.getSocketAddress());
            return;
        }
        Player player = onlinePlayers.get(address);
        System.out.println("Player: " + player);
        if (player == null) return;
        if (!(packet instanceof PlayerCommand)) return;
        PlayerCommand playerCommand = (PlayerCommand) packet;
        playerCommand.process(player);
    }

    @Override
    public void fireEvent(GameEvent event) {
        synchronized (lock) {
            events.add(event);
        }
        if (event instanceof ServerBoundEvent) {
            Stream<Player> receivers = onlinePlayers.values().stream();
            if (event instanceof LocalizedEvent) {
                receivers = receivers.filter(player -> canSee(player.getControllingEntity(), ((LocalizedEvent) event).getPosition()));
            }
            UDPConnection.getInstance().sendPacket((ServerBoundEvent) event,receivers.map(Player::getSocketAddress).collect(Collectors.toList()));
        }
    }

    public GameEngineServer(Set<Player> players) {
        instance = this;
        UDPConnection.getInstance().register(this);
        this.players = players;
        GameState.setCurrentGameState(new GameState(0));
        for (Player player : players) {
            try {
                Champion playerChamp = player.getChampion().getConstructor(UUID.class).newInstance((Object) null);
                playerChamp.setPlayer(player);
                player.setControllingEntity(playerChamp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
