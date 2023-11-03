package org.colonelkai.publictransit.listeners;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.line.travel.Travel;
import org.core.TranslateCore;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.EventListener;
import org.core.event.HEvent;
import org.core.event.events.connection.ClientConnectionEvent;
import org.core.event.events.entity.EntityDeathEvent;
import org.core.event.events.entity.EntityEvent;
import org.core.event.events.entity.EntityMoveEvent;
import org.core.world.position.impl.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class TravelListener implements EventListener {

    private static final Map<UUID, Consumer<LivePlayer>> actions = new HashMap<>();

    public static void returnPlayerToStart(UUID playerId, Position<?> position) {
        Optional<LivePlayer> optionalLivePlayer = TranslateCore
                .getServer()
                .getOnlinePlayers()
                .parallelStream()
                .filter(p -> p.getUniqueId().equals(playerId))
                .findAny();
        if (optionalLivePlayer.isPresent()) {
            LivePlayer livePlayer = optionalLivePlayer.get();
            livePlayer.setPosition(position);
            return;
        }
        actions.put(playerId, (player) -> player.setPosition(position));
    }

    public static void pausePlayer(Travel travel) {
        actions.put(travel.getPlayerId(),
                    (player) -> PublicTransit.getPlugin().getTravelManager().createSchedule(travel).run());
    }

    @HEvent
    public void onPlayerJoinEvent(ClientConnectionEvent.Incoming.Joined event) {
        LivePlayer player = event.getEntity();
        UUID playerId = player.getUniqueId();
        if (!actions.containsKey(playerId)) {
            return;
        }
        actions.get(playerId).accept(player);
        actions.remove(playerId);
    }

    @HEvent
    public void onPlayerMoveEvent(EntityMoveEvent.AsPlayer event) {
        Optional<Travel> opTravel = PublicTransit.getPlugin().getTravelManager().getTravelFromPlayer(event.getEntity());
        if (opTravel.isEmpty()) {
            return;
        }
        Travel travel = opTravel.get();
        if (travel.getLastKnownPosition().toBlockPosition().equals(event.getAfterPosition().toBlockPosition())) {
            return;
        }
        event.setCancelled(true);
    }

    @HEvent
    public void onPlayerDeathEvent(EntityDeathEvent<LivePlayer> player) {
        PublicTransit.getPlugin().getTravelManager().cancel(player.getEntity().getUniqueId());
    }
}
