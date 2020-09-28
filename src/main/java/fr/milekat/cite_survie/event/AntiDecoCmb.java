package fr.milekat.cite_survie.event;

import fr.milekat.cite_survie.MainSurvie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class AntiDecoCmb implements Listener {
    private final HashMap<Zombie, Inventory> playerInventory = new HashMap<>();
    @EventHandler
    public void onPlayerTakeHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) MainSurvie.playerCombat.put((Player) event.getDamager(), 15);
        if (event.getEntity() instanceof Player) MainSurvie.playerCombat.put((Player) event.getEntity(), 15);
    }

    @EventHandler
    public void onPlayerEnterSpawn(PlayerMoveEvent event) {
        /* Si le joueur est dans le spawn il n'est plus en mode combat */
        if (MainSurvie.playerCombat.containsKey(event.getPlayer())) {
            if (MainSurvie.isSafeSpawn.contains(event.getPlayer())) MainSurvie.playerCombat.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (MainSurvie.isSafeSpawn.contains(event.getPlayer())) {
            Zombie zombie = (Zombie) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.ZOMBIE);
            zombie.setAI(false);
            zombie.setCustomName(event.getPlayer().getName());
            zombie.setGravity(false);
            if (zombie.getEquipment()!=null && event.getPlayer().getEquipment()!=null) {
                zombie.getEquipment().setArmorContents(event.getPlayer().getEquipment().getArmorContents());
            }
            playerInventory.put(zombie, event.getPlayer().getInventory());
            event.getPlayer().setHealth(0.0D);
        }
    }

    @EventHandler
    public void onZombiePlayerDie(EntityDeathEvent event) {
        if (playerInventory.containsKey((Zombie) event.getEntity())) {
            for (ItemStack itemStack : playerInventory.get((Zombie) event.getEntity()).getContents()) {
                if (itemStack!=null) MainSurvie.WORLD.dropItemNaturally(event.getEntity().getLocation(), itemStack);
            }
            playerInventory.remove((Zombie) event.getEntity());
        }
    }
}
