package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.ConfigLoader;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.EntityService;
import uk.antiperson.stackmob.services.SupportService;

@AllArgsConstructor
public class SpawnListener implements Listener {

    private ConfigLoader config;
    private SupportService supportService;
    private EntityService entityService;
    private BukkitService bukkitService;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        final Entity entity = event.getEntity();
        final CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        // EntityTools before running task
        if (entity instanceof ArmorStand) {
            return;
        }
        if (config.get().getStringList("no-stack-reasons").contains(reason.toString())) {
            return;
        }
        if (config.get().getStringList("no-stack-types").contains(entity.getType().toString())) {
            return;
        }
        if (config.get().getStringList("no-stack-worlds").contains(entity.getWorld().getName())) {
            return;
        }

        // delay this, so the needed metadata can be set before attempting to merge.
        bukkitService.runTask(() -> {
            // EntityTools before attempting to merge with other entities
            if (entity.hasMetadata(GlobalValues.NO_SPAWN_STACK) && entity.getMetadata(GlobalValues.NO_SPAWN_STACK).get(0).asBoolean()) {
                bukkitService.removeMetadata(entity, GlobalValues.NO_SPAWN_STACK);
                return;
            }
            if (supportService.isMiniPet(entity)) {
                return;
            }

            // Check for nearby entities, and merge if compatible.
            double xLoc = config.get().getDouble("check-area.x");
            double yLoc = config.get().getDouble("check-area.y");
            double zLoc = config.get().getDouble("check-area.z");
            boolean noMatch = true;

            for (Entity nearby : entity.getNearbyEntities(xLoc, yLoc, zLoc)) {
                // EntityTools on both entities
                if (entity.getType() != nearby.getType()) {
                    continue;
                }
                if (!nearby.hasMetadata(GlobalValues.METATAG)) {
                    continue;
                }
                if (entityService.notMatching(entity, nearby)) {
                    continue;
                } else {
                    noMatch = false;
                }
                if (config.get().isInt("custom." + nearby.getType() + ".stack-max")) {
                    if (nearby.getMetadata(GlobalValues.METATAG).get(0).asInt() + 1 > config.get().getInt("custom." + nearby.getType() + ".stack-max")) {
                        continue;
                    }
                } else {
                    if (nearby.getMetadata(GlobalValues.METATAG).get(0).asInt() + 1 > config.get().getInt("stack-max")) {
                        continue;
                    }
                }

                // Continue to stack, if match is found
                entity.remove();
                int oldSize = nearby.getMetadata(GlobalValues.METATAG).get(0).asInt();
                bukkitService.setMetadata(entity, GlobalValues.METATAG, oldSize + 1);

                return;
            }

            if (config.get().getInt("dont-stack-until") > 0 && noMatch) {
                if (entityService.notEnoughNearby(entity)) {
                    bukkitService.setMetadata(entity, GlobalValues.NOT_ENOUGH_NEAR, true);
                }
            } else {
                // No match was found
                bukkitService.setMetadata(entity, GlobalValues.METATAG, 1);
            }

            // Set mcMMO stuff
            supportService.setMcMMOMetadata(entity);
        });
    }

}
