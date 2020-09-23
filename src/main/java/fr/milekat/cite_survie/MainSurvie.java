package fr.milekat.cite_survie;

import fr.milekat.cite_survie.event.HammerMine;
import fr.milekat.cite_survie.utils.HammerCraft;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class MainSurvie extends JavaPlugin {
    private static MainSurvie mainSurvie;
    @Override
    public void onEnable() {
        mainSurvie = this;
        // Events
        getServer().getPluginManager().registerEvents(new HammerMine(),this);
        Bukkit.addRecipe(new HammerCraft().createDiamsHammer());
        Bukkit.addRecipe(new HammerCraft().createIronHammer());
    }

    @Override
    public void onDisable() {
        Bukkit.removeRecipe(new NamespacedKey(MainSurvie.getMainSurvie(), "diamond_hammer"));
        Bukkit.removeRecipe(new NamespacedKey(MainSurvie.getMainSurvie(), "iron_hammer"));
    }

    public static MainSurvie getMainSurvie() {
        return mainSurvie;
    }
}
