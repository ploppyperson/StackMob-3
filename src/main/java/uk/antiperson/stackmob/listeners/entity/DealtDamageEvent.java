package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import uk.antiperson.stackmob.StackMob;

public class DealtDamageEvent implements Listener {

    private StackMob sm;
    public DealtDamageEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onDamageDealt(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player){
            if(sm.getStackTools().hasValidStackData(event.getDamager())){
                int stackSize = sm.getStackTools().getSize(event.getDamager());
                double extraDamage = event.getDamage() + ((event.getDamage() * (stackSize - 1)) * 0.2);
                event.setDamage(extraDamage);
            }
        }
    }

}
