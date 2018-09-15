package uk.antiperson.stackmob.listeners.chunk;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class UnloadEvent implements Listener {

    private StackMob sm;

    public UnloadEvent(StackMob sm) {
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
            // Add to cache
            if(!(GeneralTools.hasInvalidMetadata(currentEntity))){
                sm.cache.amountCache.put(currentEntity.getUniqueId(), currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt());
            }else if(currentEntity.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR) &&
                    currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).size() > 0 &&
                    currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()){
                sm.cache.amountCache.put(currentEntity.getUniqueId(), -1);
            }
        }
    }

}
