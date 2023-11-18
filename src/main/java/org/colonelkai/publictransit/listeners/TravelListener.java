package org.colonelkai.publictransit.listeners;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.line.travel.Travel;
import org.colonelkai.publictransit.line.travel.TravelSchedule;
import org.core.TranslateCore;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.EventListener;
import org.core.event.HEvent;
import org.core.event.events.connection.ClientConnectionEvent;
import org.core.event.events.entity.EntityDeathEvent;
import org.core.event.events.entity.EntityMoveEvent;
import org.core.world.position.impl.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class TravelListener implements EventListener {

    private static final Map<UUID, Consumer<LivePlayer>> actions = new HashMap<>();

    @HEvent
    public void onPlayerDeathEvent(EntityDeathEvent<?> event) {
        if (!(event.getEntity() instanceof LivePlayer player)) {
            return;
        }
        PublicTransit.getPlugin().getTravelManager().cancel(player.getUniqueId());
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
        if (travel.hasArrived()) {
            return;
        }
        boolean isOther = TranslateCore
                .getScheduleManager()
                .getSchedules()
                .stream()
                .filter(scheduler -> scheduler.getRunner() instanceof TravelSchedule)
                .map(sch -> (TravelSchedule) sch.getRunner())
                .filter(schedule -> schedule.getTravel().equals(travel))
                .findAny()
                .map(TravelSchedule::isTeleporting)
                .orElse(false);
        if (isOther) {
            return;
        }

        event.setCancelled(true);
    }

    public static void pausePlayer(Travel travel) {
        actions.put(travel.getPlayerId(),
                    (player) -> PublicTransit.getPlugin().getTravelManager().createSchedule(travel).run());
    }

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
}
