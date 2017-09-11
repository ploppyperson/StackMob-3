package uk.antiperson.stackmob.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class DamageReceived implements Listener {

    private StackMob sm;

    public DamageReceived(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onDamageReceived(EntityDamageEvent event) {
        if(!sm.config.getCustomConfig().getStringList("multiply-damage-received.cause-blacklist").contains(event.getCause().toString())) {
            if (event.getEntity().hasMetadata(GlobalValues.metaTag)) {
                int stackSize = event.getEntity().getMetadata(GlobalValues.metaTag).get(0).asInt();
                double extraDamage = event.getDamage() + ((event.getDamage() * (stackSize - 1)) * 0.25);
                event.setDamage(extraDamage);
            }
        }
    }
}
