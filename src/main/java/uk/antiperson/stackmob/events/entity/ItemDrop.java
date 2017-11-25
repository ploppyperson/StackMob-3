package uk.antiperson.stackmob.events.entity;

import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.concurrent.ThreadLocalRandom;

public class ItemDrop implements Listener {

    private StackMob sm;

    public ItemDrop(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onEggDrop(ItemSpawnEvent event) {
        if(event.getEntity().getItemStack().getType() == Material.EGG && !event.getEntity().hasMetadata(GlobalValues.MULTIPLIED_EGG)){
            for(Entity e : event.getEntity().getNearbyEntities(0, 0.2, 0)){
                if(e instanceof Chicken && e.hasMetadata(GlobalValues.METATAG)){
                    int stackSize = e.getMetadata(GlobalValues.METATAG).get(0).asInt();
                    int dropAmount = (int) Math.round(stackSize * (ThreadLocalRandom.current().nextDouble(0.5) + 0.35));
                    sm.dropTools.dropEggs(event.getEntity().getItemStack(), dropAmount, e.getLocation().add(0.2, 0, 0.2));
                    break;
                }
            }
        }
    }
}
