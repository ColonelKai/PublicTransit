package org.colonelkai.publictransit;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.travel.Travel;
import org.colonelkai.publictransit.line.travel.TravelSchedule;
import org.colonelkai.publictransit.listeners.TravelListener;
import org.core.TranslateCore;
import org.core.entity.living.human.player.User;
import org.core.schedule.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TravelManager {
    private Optional<Travel> getTravelFromPlayer(@NotNull UUID uuid) {
        return TranslateCore
                .getScheduleManager()
                .getSchedules()
                .parallelStream()
                .filter(scheduler -> scheduler.getRunner() instanceof TravelSchedule)
                .map(scheduler -> (TravelSchedule) scheduler.getRunner())
                .map(TravelSchedule::getTravel)
                .filter(t -> t.getPlayerId().equals(uuid))
                .findAny();
    }

    public Optional<Travel> getTravelFromPlayer(@NotNull User player) {
        return this.getTravelFromPlayer(player.getUniqueId());
    }

    public void cancel(@NotNull UUID uuid) {
        TranslateCore
                .getScheduleManager()
                .getSchedules()
                .parallelStream()
                .filter(scheduler -> scheduler.getRunner() instanceof TravelSchedule)
                .filter(scheduler -> ((TravelSchedule) scheduler.getRunner()).getTravel().getPlayerId().equals(uuid))
                .forEach(Scheduler::cancel);
    }

    public void cancelLine(Line line) {
        TranslateCore
                .getScheduleManager()
                .getSchedules()
                .parallelStream()
                .filter(scheduler -> scheduler.getRunner() instanceof TravelSchedule)
                .filter(scheduler -> ((TravelSchedule) scheduler.getRunner())
                        .getTravel()
                        .getTravellingOn()
                        .equals(line))
                .forEach(scheduler -> {
                    Travel travel = ((TravelSchedule) scheduler.getRunner()).getTravel();
                    TravelListener.returnPlayerToStart(travel.getPlayerId(), travel.getOriginalPosition());
                    scheduler.cancel();
                });
    }

    public Scheduler createSchedule(Travel travel) {
        return TravelSchedule.schedule(travel);
    }

}
