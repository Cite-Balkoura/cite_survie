package fr.milekat.cite_survie.event;

import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.bungee.ServersManagerSendPlayer;
import fr.milekat.cite_libs.MainLibs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SendToEnd implements Listener {
    @EventHandler
    public void onPlayerEndFrame(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).getType().equals(Material.END_PORTAL)) return;
        if (event.getPlayer().getBedSpawnLocation()!=null) {
            event.getPlayer().teleport(event.getPlayer().getBedSpawnLocation());
        } else {
            event.getPlayer().teleport(MainCore.locLabels.getOrDefault(
                    MainLibs.getInstance().getConfig().getString("redis.thischannel") + "_boat",
                    MainCore.defaultLocation));
        }
        new ServersManagerSendPlayer().sendPlayerToServer(event.getPlayer(),"survie_end", "_spawn");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getLocation().getY() <= 0) {
            event.getPlayer().teleport(MainCore.locLabels.getOrDefault("end_spawn",
                    new Location(Bukkit.getWorld("world_the_end"),0,150,0)));
        }
        try {
            Connection connection = MainLibs.getSql();
            PreparedStatement q = connection.prepareStatement("INSERT INTO `balkoura_last_survie`(`uuid`, `server`) " +
                    "VALUES (?, ?) ON DUPLICATE KEY UPDATE `server` = ?;");
            q.setString(1, event.getPlayer().getUniqueId().toString());
            q.setString(2, MainLibs.getInstance().getConfig().getString("redis.thischannel"));
            q.setString(3, MainLibs.getInstance().getConfig().getString("redis.thischannel"));
            q.execute();
            q.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
