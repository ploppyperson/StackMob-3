package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.ConfigLoader;

@AllArgsConstructor
public class EntityTargetListener implements Listener {

    private ConfigLoader config;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player && event.getEntity() instanceof Monster) {
            if (event.getEntity().hasMetadata(GlobalValues.METATAG)) {
                if (!config.get().getStringList("no-targeting.types-blacklist").contains(event.getEntityType().toString())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
