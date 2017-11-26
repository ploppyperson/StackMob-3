package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.Config;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.EntityService;
import uk.antiperson.stackmob.utils.BukkitVersion;

@AllArgsConstructor
public class InteractListener implements Listener {

    private Config config;
    private EntityService entityService;
    private BukkitService bukkitService;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!entity.hasMetadata(GlobalValues.METATAG)) {
            return;
        }
        if (entity.hasMetadata(GlobalValues.CURRENTLY_BREEDING) && entity.getMetadata(GlobalValues.CURRENTLY_BREEDING).get(0).asBoolean()) {
            return;
        }
        if (entity.getMetadata(GlobalValues.METATAG).get(0).asInt() == 1) {
            return;
        }

        if (entity instanceof Animals) {
            if (correctFood(event.getPlayer().getItemInHand(), entity) && ((Animals) entity).canBreed() && config.get().getBoolean("divide-on.breed")) {
                int stackSize = entity.getMetadata(GlobalValues.METATAG).get(0).asInt();

                Entity newEntity = entityService.duplicate(entity);
                bukkitService.setMetadata(newEntity, GlobalValues.METATAG, stackSize - 1);
                bukkitService.setMetadata(newEntity, GlobalValues.NO_SPAWN_STACK, true);

                bukkitService.setMetadata(entity, GlobalValues.METATAG, 1);
                bukkitService.setMetadata(entity, GlobalValues.NO_STACK_ALL, true);
                bukkitService.setMetadata(entity, GlobalValues.CURRENTLY_BREEDING, true);
                entity.setCustomName(null);

                // Allow to stack after breeding
                bukkitService.runTaskLater(() -> {
                    if (!entity.isDead()) {
                        bukkitService.setMetadata(entity, GlobalValues.NO_STACK_ALL, false);
                        bukkitService.setMetadata(entity, GlobalValues.CURRENTLY_BREEDING, false);
                    }
                }, 20 * 20);
            } else if (event.getPlayer().getItemInHand().getType() == Material.NAME_TAG && config.get().getBoolean("divide-on.name")) {
                if (entity.hasMetadata(GlobalValues.METATAG)) {
                    if (entity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1) {
                        Entity dupe = entityService.duplicate(entity);
                        bukkitService.setMetadata(dupe, GlobalValues.METATAG, entity.getMetadata(GlobalValues.METATAG).get(0).asInt() - 1);
                        bukkitService.setMetadata(dupe, GlobalValues.NO_SPAWN_STACK, true);
                    }
                    bukkitService.removeMetadata(entity, GlobalValues.METATAG);
                    bukkitService.setMetadata(entity, GlobalValues.NO_STACK_ALL, true);
                }
            }
        }

    }

    // There should be a method in bukkit for this...
    private boolean correctFood(ItemStack is, Entity entity) {
        if ((entity instanceof Cow || entity instanceof Sheep) && is.getType() == Material.WHEAT) {
            return true;
        }
        if ((entity instanceof Pig) && is.getType() == Material.CARROT_ITEM) {
            return true;
        }
        if ((entity instanceof Chicken) && is.getType().toString().contains("SEED")) {
            return true;
        }
        if (entity instanceof Horse && (is.getType() == Material.GOLDEN_APPLE || is.getType() == Material.GOLDEN_CARROT)) {
            if (((Horse) entity).isTamed()) {
                return true;
            }
        }
        if (entity instanceof Wolf && ((Wolf) entity).isTamed()) {
            if (is.getType().toString().contains("RAW") || is.getType().toString().contains("COOKED") &&
                    !is.getType().toString().contains("FISH")) {
                return true;
            }
        }
        if (entity instanceof Ocelot && is.getType() == Material.RAW_FISH && ((Ocelot) entity).isTamed()) {
            return true;
        }
        if (entity instanceof Rabbit && (is.getType() == Material.CARROT_ITEM || is.getType() == Material.GOLDEN_CARROT
                || is.getType() == Material.YELLOW_FLOWER)) {
            return true;
        }
        if (BukkitVersion.isAtLeast(BukkitVersion.V1_11)) {
            if (entity instanceof Llama && is.getType() == Material.HAY_BLOCK) {
                return true;
            }
        }
        return false;
    }

}
