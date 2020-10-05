package fr.milekat.cite_survie.event;

import fr.milekat.cite_survie.MainSurvie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AntiAFK implements Listener {
    @EventHandler
    private void onQuitResetAFK(PlayerJoinEvent event) {
        MainSurvie.timesPlayerAFK.remove(event.getPlayer());
    }
}
