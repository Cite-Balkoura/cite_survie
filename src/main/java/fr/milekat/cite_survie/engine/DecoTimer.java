package fr.milekat.cite_survie.engine;

import fr.milekat.cite_survie.MainSurvie;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class DecoTimer {
    public BukkitTask runTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, Integer> loop : MainSurvie.playerCombat.entrySet()) {
                    if (loop.getValue() < 1) {
                        MainSurvie.playerCombat.remove(loop.getKey());
                        continue;
                    }
                    loop.getKey().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                            "§cEn combat: §b" + loop.getValue() + "§c secondes"));
                    MainSurvie.playerCombat.put(loop.getKey(),loop.getValue() - 1);
                }
            }
        }.runTaskTimerAsynchronously(MainSurvie.getMainSurvie(), 0L, 20L);
    }
}
