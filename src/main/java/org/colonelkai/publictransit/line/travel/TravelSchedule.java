package org.colonelkai.publictransit.line.travel;

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

    public TravelSchedule(@NotNull Travel travel) {
        this.travel = travel;
    }

    public Travel getTravel() {
        return this.travel;
    }

    @Override
    public void accept(Scheduler scheduler) {
        if (TranslateCore
                .getServer()
                .getOnlinePlayers()
                .stream()
                .noneMatch(p -> p.getUniqueId().equals(this.travel.getPlayerId()))) {
            TravelListener.pausePlayer(this.travel);
            scheduler.cancel();
            return;
        }

        Optional<Travel> opTravel = this.travel.travelToNext();
        if (opTravel.isEmpty()) {
            scheduler.cancel();
            return;
        }
        schedule(opTravel.get()).run();
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
