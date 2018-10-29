package uk.antiperson.stackmob.listeners.chunk;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.StackTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class UnloadEvent implements Listener {

    private StackMob sm;

    public UnloadEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        if(sm.getCustomConfig().getStringList("no-stack-worlds")
                .contains(e.getWorld().getName())){
            return;
        }
        for(Entity currentEntity : e.getChunk().getEntities()){
            // Check if entity is a mob, since they despawn on chunk unload.
            if(currentEntity instanceof Monster){
                continue;
            }
            // Add to storage
            if(sm.getStackTools().hasValidData(currentEntity)){
                int stackSize = sm.getStackTools().getSize(currentEntity);
                sm.storageManager.getStackStorage().getAmountCache().put(currentEntity.getUniqueId(), stackSize);
            }
        }
    }

}
