package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.Config;
import uk.antiperson.stackmob.config.ConfigLoader;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.EntityService;

@AllArgsConstructor
public class SheepDyeListener implements Listener {

    private Config config;
    private EntityService entityService;
    private BukkitService bukkitService;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSheepDye(SheepDyeWoolEvent event) {
        if (!event.getEntity().hasMetadata(GlobalValues.METATAG)) {
            return;
        }
        int stackSize = event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt();
        Entity oldEntity = event.getEntity();

        if (config.get().getBoolean("divide-on.sheep-dye") && stackSize > 1) {
            Sheep newEntity = (Sheep) entityService.duplicate(oldEntity);
            newEntity.setColor(event.getEntity().getColor());
            bukkitService.setMetadata(newEntity, GlobalValues.METATAG, stackSize - 1);
            bukkitService.setMetadata(newEntity, GlobalValues.NO_SPAWN_STACK, true);

            bukkitService.setMetadata(oldEntity, GlobalValues.METATAG, 1);
            oldEntity.setCustomName(null);
        }
    }

}
