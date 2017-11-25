package uk.antiperson.stackmob.tasks;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.ConfigLoader;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.EntityService;

@AllArgsConstructor
// TODO: Neaten this stuff up, but I cba.
public class StackTask extends BukkitRunnable {

    private ConfigLoader config;
    private EntityService entityService;
    private BukkitService bukkitService;

    @Override
    public void run() {
        double xLoc = config.get().getDouble("check-area.x");
        double yLoc = config.get().getDouble("check-area.y");
        double zLoc = config.get().getDouble("check-area.z");

        // Worlds loop, and check that the world isn't blacklisted
        for (World world : Bukkit.getWorlds()) {
            if (config.get().getStringList("no-stack-worlds").contains(world.getName())) {
                continue;
            }
            // Loop all entities in the current world
            for (Entity first : world.getLivingEntities()) {
                // Checks on first entity
                if (entityService.notTaskSuitable(first)) {
                    continue;
                }

                if (first.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR)
                        && first.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()) {
                    entityService.notEnoughNearby(first);
                }

                // Find nearby entities
                for (Entity nearby : first.getNearbyEntities(xLoc, yLoc, zLoc)) {

                    //Checks on nearby
                    if (first.getType() != nearby.getType()) {
                        continue;
                    }

                    if (entityService.notTaskSuitable(nearby)) {
                        continue;
                    }

                    if (nearby.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR)
                            && nearby.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()) {
                        continue;
                    }

                    // Check attributes of both
                    if (entityService.notMatching(first, nearby)) {
                        continue;
                    }

                    int nearbySize = nearby.getMetadata(GlobalValues.METATAG).get(0).asInt();
                    int firstSize;
                    if (first.hasMetadata(GlobalValues.METATAG)) {
                        firstSize = first.getMetadata(GlobalValues.METATAG).get(0).asInt();
                    } else {
                        firstSize = 1;
                    }

                    int maxSize;
                    if (config.get().isInt("custom." + nearby.getType() + ".stack-max")) {
                        maxSize = config.get().getInt("custom." + nearby.getType() + ".stack-max");
                    } else {
                        maxSize = config.get().getInt("stack-max");
                    }

                    // Nearby would normally get removed, but we're swapping it.
                    if (nearbySize > firstSize && config.get().getBoolean("big-priority")) {
                        Entity holder = nearby;
                        nearby = first;
                        first = holder;
                    }

                    // Continue to stack together
                    int amountTotal = nearbySize + firstSize;
                    if (amountTotal > maxSize) {
                        bukkitService.setMetadata(first, GlobalValues.METATAG, maxSize);
                        bukkitService.setMetadata(nearby, GlobalValues.METATAG, amountTotal - maxSize);
                    } else {
                        bukkitService.setMetadata(first, GlobalValues.METATAG, amountTotal);
                        nearby.remove();
                    }

                    break;
                }
            }
        }
    }

}
