package fr.milekat.cite_survie.event;

import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PiglinTradeDisable implements Listener {
    @EventHandler (priority = EventPriority.LOW)
    public void onPiglinTake(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Piglin) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPiglinGive(EntityDropItemEvent event) {
        if (event.getEntity() instanceof Piglin) {
            event.setCancelled(true);
        }
    }
}
