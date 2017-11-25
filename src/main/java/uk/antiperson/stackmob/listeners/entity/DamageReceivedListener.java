package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.config.ConfigLoader;

@AllArgsConstructor
public class DamageReceivedListener implements Listener {

    private ConfigLoader config;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamageReceived(EntityDamageEvent event) {
        if (!config.get().getStringList("multiply-damage-received.cause-blacklist").contains(event.getCause().toString())) {
            if (event.getEntity().hasMetadata(GlobalValues.METATAG)) {
                int stackSize = event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt();
                double extraDamage = event.getDamage() + ((event.getDamage() * (stackSize - 1)) * 0.25);
                event.setDamage(extraDamage);
            }
        }
    }
}
