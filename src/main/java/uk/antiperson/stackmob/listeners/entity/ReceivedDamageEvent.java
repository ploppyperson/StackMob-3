package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class ReceivedDamageEvent implements Listener {

    private StackMob sm;

    public ReceivedDamageEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onDamageReceived(EntityDamageEvent event) {
        if(!sm.config.getCustomConfig().getStringList("multiply-damage-received.cause-blacklist").contains(event.getCause().toString())) {
            if (GeneralTools.hasInvaildMetadata(event.getEntity())) {
                int stackSize = event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt();
                double extraDamage = event.getDamage() + ((event.getDamage() * (stackSize - 1)) * 0.25);
                event.setDamage(extraDamage);
            }
        }
    }
}
