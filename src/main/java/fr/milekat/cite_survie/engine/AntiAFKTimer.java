package fr.milekat.cite_survie.engine;

import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_libs.utils_tools.Jedis.JedisPub;
import fr.milekat.cite_survie.MainSurvie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AntiAFKTimer {
    public BukkitTask runTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    if (MainSurvie.lastPlayerLocation.getOrDefault(player,MainSurvie.SPAWN).distance(player.getLocation())<15) {
                        MainSurvie.timesPlayerAFK.put(player,MainSurvie.timesPlayerAFK.getOrDefault(player,0) + 1);
                        if (MainSurvie.timesPlayerAFK.getOrDefault(player,0)==5) {
                            player.sendMessage(MainCore.prefixCmd + "§6Tu vas finir par t'engourdir à bouger si peut.");
                        } else if (MainSurvie.timesPlayerAFK.getOrDefault(player,0)==7) {
                            player.sendMessage(MainCore.prefixCmd + "§6Il est temps de bouger, §ctu sembles afk§6.");
                        } else if (MainSurvie.timesPlayerAFK.getOrDefault(player,0)==9) {
                            player.sendMessage(MainCore.prefixCmd +
                                    "§cTu sera ejecté dans 1 minute pour afk si tu ne bouges pas plus.");
                        } else if (MainSurvie.timesPlayerAFK.getOrDefault(player,0)>=10) {
                            Bukkit.getScheduler().runTask(MainSurvie.getMainSurvie(),()->
                                    player.kickPlayer(MainCore.prefixCmd + System.lineSeparator() +
                                            "§cVous avez été kick pour la raison suivante:" + System.lineSeparator() +
                                            "§eKick pour AFK."));
                            JedisPub.sendRedis("log_sanction#:#kick#:#" +
                                    MainCore.profilHashMap.get(player.getUniqueId()).getDiscordid() +
                                    "#:#console#:#null#:#null#:#Kick pour AFK#:#/kick " + player.getName() + " Kick pour AFK");
                        }
                    } else MainSurvie.timesPlayerAFK.remove(player);
                    MainSurvie.lastPlayerLocation.put(player,player.getLocation());
                }
            }
        }.runTaskTimerAsynchronously(MainSurvie.getMainSurvie(), 0L, 1200L);
    }
}
