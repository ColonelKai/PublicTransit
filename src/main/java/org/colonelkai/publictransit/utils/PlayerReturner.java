package org.colonelkai.publictransit.utils;

import org.core.TranslateCore;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.HEvent;
import org.core.event.events.connection.ClientConnectionEvent;
import org.core.world.position.impl.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerReturner {
    // Might be a horrible solution, I don't care. I want to get this project done with minimal effort :sob:

    private static final Map<UUID, Position> playersToBeReturned = new HashMap<>();

    public static void sendBackThePlayer(UUID player, Position position) {
        Optional<LivePlayer> optionalLivePlayer
                = TranslateCore.getServer().getOnlinePlayers()
                .parallelStream()
                .filter(p -> p.getUniqueId().equals(player))
                .findAny();
        if(optionalLivePlayer.isEmpty()) {
            playersToBeReturned.put(player, position);
        }
        LivePlayer livePlayer = optionalLivePlayer.get();

        livePlayer.setPosition(position);
    }

    @HEvent
    public void onPlayerJoinEvent(ClientConnectionEvent.Incoming.Joined event) {
        LivePlayer player = event.getEntity();

        playersToBeReturned
                .forEach((uuid, position) -> {
                    if(player.getUniqueId().equals(uuid)) {
                        player.setPosition(position);
                    }
                });
    }
}
