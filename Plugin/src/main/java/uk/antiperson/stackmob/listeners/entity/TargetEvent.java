package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.entity.StackTools;

public class TargetEvent implements Listener {

    private IStackMob sm;
    public TargetEvent(IStackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if(event.getTarget() instanceof Player && event.getEntity() instanceof Monster){
            if(StackTools.hasSizeMoreThanOne(event.getEntity())){
                if(!sm.getCustomConfig().getStringList("no-targeting.types-blacklist")
                        .contains(event.getEntityType().toString())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
