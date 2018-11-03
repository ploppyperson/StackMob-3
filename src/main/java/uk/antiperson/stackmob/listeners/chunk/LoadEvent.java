package uk.antiperson.stackmob.listeners.chunk;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import uk.antiperson.stackmob.StackMob;

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
            if(sm.getStorageManager().getStackStorage().getAmountCache().containsKey(currentEntity.getUniqueId())){
                int cacheSize = sm.getStorageManager().getStackStorage().getAmountCache().get(currentEntity.getUniqueId());
                sm.getStorageManager().getStackStorage().getAmountCache().remove(currentEntity.getUniqueId());
                sm.getStackTools().setSize(currentEntity, cacheSize);
            }
        }
    }

}
