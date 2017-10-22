package uk.antiperson.stackmob.events.chunk;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class ChunkLoad implements Listener {

    private StackMob sm;

    public ChunkLoad(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        if(sm.config.getCustomConfig().getStringList("no-stack-worlds")
                .contains(e.getWorld().getName())){
            return;
        }
        for(Entity currentEntity : e.getChunk().getEntities()){
            // Check if has been cached.
            if(!sm.cache.amountCache.containsKey(currentEntity.getUniqueId())){
                continue;
            }
            if(sm.cache.amountCache.get(currentEntity.getUniqueId()) == -69){
                currentEntity.setMetadata(GlobalValues.NOT_ENOUGH_NEAR, new FixedMetadataValue(sm, true));
            }else{
                currentEntity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, sm.cache.amountCache.get(currentEntity.getUniqueId())));
            }

            // Cleanup.
            sm.cache.amountCache.remove(currentEntity.getUniqueId());

        }
    }

}
