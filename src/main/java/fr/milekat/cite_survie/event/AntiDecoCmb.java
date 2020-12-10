package fr.milekat.cite_survie.event;

import fr.milekat.cite_survie.MainSurvie;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AntiDecoCmb implements Listener {
    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        MainSurvie.lastPlayerLocation.put(event.getPlayer(),event.getPlayer().getLocation());
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerTakeHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            MainSurvie.playerCombat.put((Player) event.getDamager(), 15);
            MainSurvie.playerCombat.put((Player) event.getEntity(), 15);
        }
    }

    @EventHandler
    public void onPlayerEnterSpawn(PlayerMoveEvent event) {
        /* Si le joueur est dans le spawn il n'est plus en mode combat */
        if (MainSurvie.playerCombat.containsKey(event.getPlayer())) {
            if (MainSurvie.isSafeSpawn.contains(event.getPlayer())) MainSurvie.playerCombat.remove(event.getPlayer());
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (MainSurvie.playerCombat.containsKey(event.getPlayer())) {
            Zombie zombie = (Zombie) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.ZOMBIE);
            zombie.setAI(false);
            zombie.setCustomName(event.getPlayer().getName());
            zombie.setGravity(false);
            if (zombie.getEquipment()!=null && event.getPlayer().getEquipment()!=null) {
                zombie.getEquipment().setArmorContents(event.getPlayer().getEquipment().getArmorContents());
            }
            if (zombie.getEquipment()!=null) {
                zombie.getEquipment().setItemInMainHand(event.getPlayer().getInventory().getItemInMainHand());
            }
            Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);
            inventory.setContents(event.getPlayer().getInventory().getContents());
            MainSurvie.playerInventory.put(zombie, inventory);
            event.getPlayer().getInventory().clear();
            event.getPlayer().setHealth(0);
            event.getPlayer().spigot().respawn();
        }
    }

    @EventHandler
    public void onZombiePlayerDie(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie && MainSurvie.playerInventory.containsKey((Zombie) event.getEntity())) {
            event.getDrops().clear();
            for (ItemStack itemStack : MainSurvie.playerInventory.get((Zombie) event.getEntity()).getContents()) {
                if (itemStack!=null) MainSurvie.WORLD.dropItemNaturally(event.getEntity().getLocation(), itemStack);
            }
            MainSurvie.playerInventory.remove((Zombie) event.getEntity());
        }
    }
}
