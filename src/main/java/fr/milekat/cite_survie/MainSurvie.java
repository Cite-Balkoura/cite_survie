package fr.milekat.cite_survie;

import fr.milekat.cite_survie.engine.DecoTimer;
import fr.milekat.cite_survie.event.AntiDecoCmb;
import fr.milekat.cite_survie.event.ElytraSpeedDisable;
import fr.milekat.cite_survie.event.HammerMine;
import fr.milekat.cite_survie.event.SpawnProtect;
import fr.milekat.cite_survie.utils.HammerCraft;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class MainSurvie extends JavaPlugin {
    private static MainSurvie mainSurvie;
    public static World WORLD;
    public static ArrayList<Player> isSafeSpawn = new ArrayList<org.bukkit.entity.Player>();
    public static HashMap<Player, Integer> playerCombat = new HashMap<Player, Integer>();
    private BukkitTask timerDeco;

    @Override
    public void onEnable() {
        mainSurvie = this;
        WORLD = Bukkit.getWorld("world");
        // Events
        getServer().getPluginManager().registerEvents(new HammerMine(),this);
        getServer().getPluginManager().registerEvents(new ElytraSpeedDisable(),this);
        getServer().getPluginManager().registerEvents(new SpawnProtect(),this);
        getServer().getPluginManager().registerEvents(new AntiDecoCmb(),this);
        Bukkit.addRecipe(new HammerCraft().createDiamsHammer());
        Bukkit.addRecipe(new HammerCraft().createIronHammer());
        timerDeco =new DecoTimer().runTask();
    }

    @Override
    public void onDisable() {
        Bukkit.removeRecipe(new NamespacedKey(MainSurvie.getMainSurvie(), "diamond_hammer"));
        Bukkit.removeRecipe(new NamespacedKey(MainSurvie.getMainSurvie(), "iron_hammer"));
        timerDeco.cancel();
    }

    public static MainSurvie getMainSurvie() {
        return mainSurvie;
    }
}
