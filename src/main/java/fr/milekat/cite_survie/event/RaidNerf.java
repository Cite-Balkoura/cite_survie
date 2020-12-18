package fr.milekat.cite_survie.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;

import java.util.ArrayList;

public class RaidNerf implements Listener {
    private final ArrayList<Player> players = new ArrayList<>();
    @EventHandler
    public void onStartRaid(RaidTriggerEvent event) {
        if (players.contains(event.getPlayer())) {
            event.setCancelled(true);
        } else {
            players.add(event.getPlayer());
        }
    }
}
