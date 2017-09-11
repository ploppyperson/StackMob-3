package uk.antiperson.stackmob.events.chunk;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class ChunkUnload implements Listener {

    private StackMob sm;

    public ChunkUnload(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        if(sm.config.getCustomConfig().getStringList("no-stack-worlds")
                .contains(e.getWorld().getName())){
            return;
        }
        for(Entity currentEntity : e.getChunk().getEntities()){
            // Check if entity is a mob, since they despawn on chunk unload.
            if(currentEntity instanceof Monster){
                continue;
            }
            // Check if stackable
            if(!currentEntity.hasMetadata(GlobalValues.metaTag)){
                continue;
            }
            // Add to cache
            sm.cache.amountCache.put(currentEntity.getUniqueId(), currentEntity.getMetadata(GlobalValues.metaTag).get(0).asInt());
        }
    }

}
