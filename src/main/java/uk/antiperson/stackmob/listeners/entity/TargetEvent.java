package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;

public class TargetEvent implements Listener {

    private StackMob sm;

    public TargetEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if(event.getTarget() instanceof Player && event.getEntity() instanceof Monster){
            if(GeneralTools.hasValidStackData(event.getEntity())){
                if(!sm.config.getCustomConfig().getStringList("no-targeting.types-blacklist").contains(event.getEntityType().toString())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
