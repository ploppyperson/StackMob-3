package uk.antiperson.stackmob.listeners;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.Config;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.storage.Cache;

@AllArgsConstructor
public class ChunkListener implements Listener {

    private Config config;
    private Cache cache;
    private BukkitService bukkitService;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (config.get().getStringList("no-stack-worlds").contains(event.getWorld().getName())) {
            return;
        }
        for (Entity currentEntity : event.getChunk().getEntities()) {
            // Check if has been cached.
            if (!cache.isCached(currentEntity.getUniqueId())) {
                continue;
            }
            if (cache.getAmount(currentEntity.getUniqueId()) == -69) {
                bukkitService.setMetadata(currentEntity, GlobalValues.NOT_ENOUGH_NEAR, true);
            } else {
                bukkitService.setMetadata(currentEntity, GlobalValues.METATAG, cache.getAmount(currentEntity.getUniqueId()));
            }

            // Cleanup.
            cache.remove(currentEntity.getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (config.get().getStringList("no-stack-worlds").contains(event.getWorld().getName())) {
            return;
        }
        for (Entity currentEntity : event.getChunk().getEntities()) {
            // Check if entity is a mob, since they despawn on chunk unload.
            if (currentEntity instanceof Monster) {
                continue;
            }
            // Check if stackable
            if (!currentEntity.hasMetadata(GlobalValues.METATAG)) {
                continue;
            }
            // Add to cache
            cache.put(currentEntity.getUniqueId(), currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt());
        }
    }

}
