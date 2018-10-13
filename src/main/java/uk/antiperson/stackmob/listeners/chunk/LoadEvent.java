package uk.antiperson.stackmob.listeners.chunk;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class LoadEvent implements Listener {

    private StackMob sm;

    public LoadEvent(StackMob sm) {
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
            if(sm.storageManager.getStackStorage().getAmountCache().containsKey(currentEntity.getUniqueId())){
                int cacheSize = sm.storageManager.getStackStorage().getAmountCache().get(currentEntity.getUniqueId());
                if(cacheSize == -69 || cacheSize == -1){
                    currentEntity.setMetadata(GlobalValues.NOT_ENOUGH_NEAR, new FixedMetadataValue(sm, true));
                }else{
                    currentEntity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, cacheSize));
                }

                // Removing this would surely make the entities keep their data, if data is lost?
                //sm.storage.amountCache.remove(currentEntity.getUniqueId());
            }
        }
    }

}
