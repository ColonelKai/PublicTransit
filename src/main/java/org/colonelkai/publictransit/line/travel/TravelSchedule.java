package org.colonelkai.publictransit.line.travel;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.listeners.TravelListener;
import org.core.TranslateCore;
import org.core.schedule.Scheduler;
import org.core.schedule.unit.TimeUnit;
import org.core.utils.ComponentUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public class TravelSchedule implements Consumer<Scheduler> {

    private final @NotNull Travel travel;
    private boolean isTeleporting;

    public TravelSchedule(@NotNull Travel travel) {
        this.travel = travel;
    }

    @Override
    public void accept(Scheduler scheduler) {
        var opPlayer = TranslateCore
                .getServer()
                .getOnlinePlayers()
                .stream()
                .filter(player -> player.getUniqueId().equals(this.travel.getPlayerId()))
                .findAny();
        if (opPlayer.isEmpty()) {
            TravelListener.pausePlayer(this.travel);
            scheduler.cancel();
            return;
        }

        Optional<Travel> opTravel = this.travel.travelToNext();
        if (opTravel.isEmpty()) {
            scheduler.cancel();
            return;
        }
        Travel travel = opTravel.get();
        opPlayer.ifPresent(livePlayer -> {
            var newPos = travel.getCurrentNode().getPosition();
            this.isTeleporting = true;
            if (!livePlayer.setPosition(newPos)) {
                livePlayer.sendMessage(Component.text("Could not teleport"));
            }
            this.isTeleporting = false;
        });
        schedule(travel).run();
    }

    public Travel getTravel() {
        return this.travel;
    }

    public boolean isTeleporting() {
        return this.isTeleporting;
    }

    public static Scheduler schedule(@NotNull Travel travel) {
        return TranslateCore
                .getScheduleManager()
                .schedule()
                .setDelay(travel.getCurrentNode().getTime())
                .setDelayUnit(TimeUnit.SECONDS)
                .setRunner(new TravelSchedule(travel))
                .setDisplayName("travel-" + ComponentUtils.toPlain(travel.getTravellingOn().getName()) + "-" + travel
                        .getPlayerId()
                        .toString())
                .buildDelayed(PublicTransit.getPlugin());
    }
}
