package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tasks.SpawnTask;

public class SpawnEvent implements Listener {

    private StackMob sm;
    public SpawnEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent e){
        LivingEntity entity = e.getEntity();

        // EntityTools before running task
        if(!(entity instanceof Mob)){
            return;
        }
        if(sm.getLogic().doSpawnChecks(entity, e.getSpawnReason().toString())){
            return;
        }
        if(sm.getLogic().makeWaiting(entity, e.getSpawnReason())){
            return;
        }
        // BukkitRunnable to delay this, so the needed metadata can be set before attempting to merge.
        new SpawnTask(sm, entity).runTask(sm);
    }


}
