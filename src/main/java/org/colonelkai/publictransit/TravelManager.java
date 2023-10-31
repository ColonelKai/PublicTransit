package org.colonelkai.publictransit;

import org.colonelkai.publictransit.line.travel.Travel;
import org.core.TranslateCore;
import org.core.entity.living.human.player.User;
import org.core.schedule.Scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class TravelManager {
    private final HashMap<Travel, Scheduler> travelMap = new HashMap<>();

    public Optional<Travel> getTravelFromPlayer(UUID uuid) {
        return this.travelMap
                .keySet()
                .parallelStream()
                .filter(
                        travel -> travel.getPlayer().equals(uuid)
                )
                .findAny();
    }

    public Optional<Travel> getTravelFromPlayer(User player) {
        return this.getTravelFromPlayer(player.getUniqueId());
    }

    private Optional<Travel> getTravelFromScheduler(Scheduler scheduler) {
        return this.travelMap.entrySet()
                .parallelStream()
                .filter((e) -> e.getValue().equals(scheduler))
                .map(Map.Entry::getKey)
                .findAny();
    }

    public Scheduler getScheduler(Travel travel) {
        return this.travelMap.get(travel);
    }

    public void updateScheduler(Travel travel, Scheduler newScheduler) {
        this.travelMap.replace(travel, newScheduler);
    }

    public void stopTravel(Travel travel, boolean returnToOriginal) {
        this.travelMap.remove(travel);
        // TODO implement returning to original if needed.
    }

    // The Scheduler Loop (So cool it'll make your head spin! It's also probably bad code.)
    /*
    enter loop by saying 'startTravel'.

    'startTravel' calls 'scheduleNextTravel'

    'scheduleNextTravel' does:
    - schedule a task to be run (consumer) that will do the moving to next node
    when the time comes, which is the current node's time
    - set runAfter so that it itself will be called after the consumer is done.

    During this, consumer checks to see if it has been removed from this list here, and
    terminates if so, not letting the runAfter schedule another scheduler to schedule. what? yeah.

    something like that.
     */

    private Scheduler scheduleNextTravel(Travel travel) {
        return TranslateCore.getScheduleManager().schedule()
                .setDelay(travel.getCurrentNode().getTime())

                // RUNNER
                .setRunner(
                        (Scheduler scheduler) -> {
                            // check if we're still on the list, or if we've been removed.
                            Optional<Travel> optionalTravel = PublicTransit.getPlugin().getTravelManager().getTravelFromScheduler(scheduler);
                            if (optionalTravel.isEmpty()) {
                                scheduler.cancel(); // double tapping
                                return;
                            }
                                optionalTravel.get().travelToNext();
                        }

                // RUN AFTER-ER
                ).setToRunAfter(
                       TranslateCore.getScheduleManager().schedule()
                               .setDelay(travel.getCurrentNode().getTime())
                               .setRunner(scheduler -> {
                                   Scheduler newScheduler = this.scheduleNextTravel(travel);
                                   PublicTransit // this replaces the new Scheduler to keep the travelMap updated.
                                           .getPlugin()
                                           .getTravelManager()
                                           .updateScheduler(travel, newScheduler);
                               })
                               .buildDelayed(PublicTransit.getPlugin())
                ).buildDelayed(PublicTransit.getPlugin());
    }

    // start any travel
    private void startTravel(Travel travel) {
        Scheduler scheduler = this.scheduleNextTravel(travel);
        this.travelMap.put(travel, scheduler);
    }

}
