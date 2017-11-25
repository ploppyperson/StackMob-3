package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class DamgeDelt implements Listener {

    private StackMob sm;

    public DamgeDelt(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onDamgeDelt(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player){
            if(event.getDamager().hasMetadata(GlobalValues.METATAG)){
                int stackSize = event.getDamager().getMetadata(GlobalValues.METATAG).get(0).asInt();
                double extraDamage = event.getDamage() + ((event.getDamage() * (stackSize - 1)) * 0.25);
                event.setDamage(extraDamage);
            }
        }
    }

}
