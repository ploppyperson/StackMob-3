package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class TargetEvent implements Listener {

    private StackMob sm;

    public TargetEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if(event.getTarget() instanceof Player && event.getEntity() instanceof Monster){
            if(event.getEntity().hasMetadata(GlobalValues.METATAG)){
                if(!sm.config.getCustomConfig().getStringList("no-targeting.types-blacklist").contains(event.getEntityType().toString())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
