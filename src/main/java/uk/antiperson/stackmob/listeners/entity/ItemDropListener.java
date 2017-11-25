package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.services.DropService;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class ItemDropListener implements Listener {

    private DropService dropService;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEggDrop(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getType() == Material.EGG && !event.getEntity().hasMetadata(GlobalValues.MULTIPLIED_EGG)) {
            for (Entity e : event.getEntity().getNearbyEntities(0, 0.2, 0)) {
                if (e instanceof Chicken && e.hasMetadata(GlobalValues.METATAG)) {
                    int stackSize = e.getMetadata(GlobalValues.METATAG).get(0).asInt();
                    int dropAmount = (int) Math.round(stackSize * (ThreadLocalRandom.current().nextDouble(0.5) + 0.35));
                    dropService.dropEggs(event.getEntity().getItemStack(), dropAmount, e.getLocation().add(0.2, 0, 0.2));
                    break;
                }
            }
        }
    }

}
