package uk.antiperson.stackmob.services;

import lombok.AllArgsConstructor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;

import java.util.UUID;

@AllArgsConstructor
public class BukkitService {

    private StackMob plugin;

    private static BukkitRunnable bukkitRunnable(Runnable runnable) {
        return new BukkitRunnable() {

            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public void setMetadata(Entity entity, String key, Object value) {
        entity.setMetadata(key, new FixedMetadataValue(plugin, value));
    }

    public void removeMetadata(Entity entity, String key) {
        entity.removeMetadata(key, plugin);
    }

    public void runTask(Runnable runnable) {
        bukkitRunnable(runnable).runTask(plugin);
    }

    public void runTaskLater(Runnable runnable, long delay) {
        bukkitRunnable(runnable).runTaskLater(plugin, delay);
    }

    public void runTaskTimer(Runnable runnable, long delay, long interval) {
        bukkitRunnable(runnable).runTaskTimer(plugin, delay, interval);
    }

    // This is needed because of 1.8
    public Entity getEntity(UUID uuid) {
        for (World world : plugin.getServer().getWorlds()) {
            for (Entity entity : world.getLivingEntities()) {
                if (entity.getUniqueId().equals(uuid)) {
                    return entity;
                }
            }
        }
        return null;
    }
}
