package uk.antiperson.stackmob.listeners.entity;

import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import uk.antiperson.stackmob.GlobalValues;

@NoArgsConstructor
public class DamageMultiplierListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamageMultiplier(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager().hasMetadata(GlobalValues.METATAG)) {
                int stackSize = event.getDamager().getMetadata(GlobalValues.METATAG).get(0).asInt();
                double extraDamage = event.getDamage() + ((event.getDamage() * (stackSize - 1)) * 0.25);
                event.setDamage(extraDamage);
            }
        }
    }

}
