package fr.milekat.cite_survie.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class HammerMine implements Listener {
    private final Map<UUID, BlockFace> lastClickedBlockFaces = new HashMap<>();
    private final Map<UUID, Location> lastClickedBlockLoc = new HashMap<>();
    private final ArrayList<BlockFace> allowedFaces= new ArrayList<>(Arrays.asList(BlockFace.NORTH,BlockFace.EAST,
            BlockFace.SOUTH,BlockFace.WEST,BlockFace.UP,BlockFace.DOWN));
    private final ArrayList<Material> allowedBlocks = new ArrayList<>(Arrays.asList(
            Material.STONE,
            Material.GRANITE,
            Material.POLISHED_GRANITE,
            Material.DIORITE,
            Material.POLISHED_DIORITE,
            Material.ANDESITE,
            Material.POLISHED_ANDESITE,
            Material.GRASS_BLOCK,
            Material.DIRT,
            Material.COARSE_DIRT,
            Material.PODZOL,
            Material.CRIMSON_NYLIUM,
            Material.WARPED_NYLIUM,
            Material.COBBLESTONE,
            Material.SAND,
            Material.RED_SAND,
            Material.GRAVEL,
            Material.GOLD_ORE,
            Material.IRON_ORE,
            Material.COAL_ORE,
            Material.NETHER_GOLD_ORE,
            Material.SPONGE,
            Material.WET_SPONGE,
            Material.LAPIS_ORE,
            Material.SANDSTONE,
            Material.GRASS,
            Material.MOSSY_COBBLESTONE,
            Material.DIAMOND_ORE,
            Material.FARMLAND,
            Material.REDSTONE_ORE,
            Material.ICE,
            Material.SNOW_BLOCK,
            Material.CLAY,
            Material.NETHERRACK,
            Material.SOUL_SAND,
            Material.SOUL_SOIL,
            Material.BASALT,
            Material.POLISHED_BASALT,
            Material.GLOWSTONE,
            Material.NETHER_BRICKS,
            Material.END_STONE,
            Material.EMERALD_ORE,
            Material.NETHER_QUARTZ_ORE));

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onHammerMine(BlockBreakEvent event) {
        if (!lastClickedBlockLoc.getOrDefault(event.getPlayer().getUniqueId(),null)
                .equals(event.getBlock().getLocation()))
            return;
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta = tool.getItemMeta();
        if (meta==null || !meta.hasCustomModelData() || meta.getCustomModelData()!=1) return;
        BlockFace blockFace = lastClickedBlockFaces.getOrDefault(event.getPlayer().getUniqueId(),null);
        if (blockFace==null || !allowedFaces.contains(blockFace)) return;
        ArrayList<BlockFace> facesToMine = new ArrayList<>(allowedFaces);
        facesToMine.remove(blockFace);
        facesToMine.remove(blockFace.getOppositeFace());
        for (BlockFace faceloop: facesToMine) {
            Block block = event.getBlock().getRelative(faceloop);
            if (allowedBlocks.contains(block.getType())) {
                block.breakNaturally(tool);
                processItemUses(tool, meta);
            }
        }
    }

    @EventHandler
    private void onBlockClick(PlayerInteractEvent event) {
        if (!event.hasBlock() || event.getClickedBlock()==null) return;
        lastClickedBlockLoc.put(event.getPlayer().getUniqueId(),event.getClickedBlock().getLocation());
        lastClickedBlockFaces.put(event.getPlayer().getUniqueId(),event.getBlockFace());
    }

    /**
     *      Ajout de durability ou non si unBreaking
     */
    private void processItemUses(ItemStack itemStack, ItemMeta meta) {
        if (!(((Damageable) meta).getDamage() == itemStack.getType().getMaxDurability())) {
            if (new Random().nextInt(100) > (100 - (100 /
                    (itemStack.getEnchantments().getOrDefault(Enchantment.DURABILITY, 0) + 1 + 1)))) {
                ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
            }
        }
        itemStack.setItemMeta(meta);
    }
}
