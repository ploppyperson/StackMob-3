package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tasks.SpawnTask;

public class SpawnEvent implements Listener {

    private StackMob sm;
    public SpawnEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e){
        LivingEntity entity = e.getEntity();

        // EntityTools before running task
        if(entity instanceof ArmorStand){
            return;
        }
        if(sm.getCustomConfig().getStringList("no-stack-reasons")
                .contains(e.getSpawnReason().toString())){
            return;
        }
        if(sm.getCustomConfig().getStringList("no-stack-types")
                .contains(entity.getType().toString())){
            return;
        }
        if(sm.getCustomConfig().getStringList("no-stack-worlds")
                .contains(entity.getWorld().getName())){
            return;
        }

        // BukkitRunnable to delay this, so the needed metadata can be set before attempting to merge.
        new SpawnTask(sm, entity).runTaskLater(sm,  1);
    }

}
