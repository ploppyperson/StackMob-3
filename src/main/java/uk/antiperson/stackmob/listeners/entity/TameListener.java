package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.EntityService;

@AllArgsConstructor
public class TameListener implements Listener {

    private BukkitService bukkitService;
    private EntityService entityService;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onTame(EntityTameEvent event) {
        Entity entity = event.getEntity();
        if (entity.hasMetadata(GlobalValues.METATAG)) {
            if (entity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1) {
                Entity dupe = entityService.duplicate(event.getEntity());
                bukkitService.setMetadata(dupe, GlobalValues.METATAG, entity.getMetadata(GlobalValues.METATAG).get(0).asInt() - 1);
                bukkitService.setMetadata(dupe, GlobalValues.NO_SPAWN_STACK, true);
            }
            bukkitService.setMetadata(entity, GlobalValues.METATAG, 0);
            bukkitService.setMetadata(entity, GlobalValues.NO_STACK_ALL, true);
            entity.setCustomName(null);
            entity.setCustomNameVisible(false);
        }
    }

}
