package fr.milekat.cite_survie.event;

import fr.milekat.cite_libs.MainLibs;
import fr.milekat.cite_libs.core.events_register.RedisMessageReceive;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class SetPlayerLoc implements Listener {
    private final HashMap<String, Location> newPlayerLoc = new HashMap<>();
    @EventHandler
    public void onRedisNewPlayerLoc(RedisMessageReceive event) {
        if (!event.getLabel().equalsIgnoreCase(MainLibs.getInstance().getConfig().getString("other.server_name"))) return;
        if (!event.getArgs()[0].equalsIgnoreCase("set_position")) return;
        if (event.getArgs()[2].equalsIgnoreCase("label") && event.getArgs().length == 4) {
            newPlayerLoc.put(event.getArgs()[1], new Location(Bukkit.getWorld("world"),
                Double.parseDouble(MainLibs.getInstance().getConfig().getString("other.loc_label." + event.getArgs()[3] + ".x")),
                Double.parseDouble(MainLibs.getInstance().getConfig().getString("other.loc_label." + event.getArgs()[3] + ".y")),
                Double.parseDouble(MainLibs.getInstance().getConfig().getString("other.loc_label." + event.getArgs()[3] + ".z"))
            ));
        } else if (event.getArgs().length == 3) {
            /* coordonnées : "x;y;z" */
            String[] coords = event.getArgs()[2].split(";");
            newPlayerLoc.put(event.getArgs()[1], new Location(Bukkit.getWorld("world"),
                    Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]),
                    Double.parseDouble(coords[2])
            ));
        } else {
            Bukkit.getLogger().info("Erreur arguments: " + event.getFullMessage());
        }
    }

    @EventHandler
    public void onJoinTeleport(PlayerJoinEvent event) {
        if (newPlayerLoc.containsKey(event.getPlayer().getName())) {
            event.getPlayer().teleport(newPlayerLoc.get(event.getPlayer().getName()));
            newPlayerLoc.remove(event.getPlayer().getName());
        }
    }
}
