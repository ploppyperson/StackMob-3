package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import uk.antiperson.stackmob.api.StackMob;
import uk.antiperson.stackmob.api.entity.StackTools;

import java.util.concurrent.ThreadLocalRandom;

public class ItemDrop implements Listener {

    private StackMob sm;

    public ItemDrop(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onEggDrop(ItemSpawnEvent event) {
        if(event.getEntity().getItemStack().getType() == Material.EGG){
            // You might be wondering why, but it's to prevent the eggs being multiplied.
            // Metadata doesn't work because this event is fired before the metadata would be set.
            if(event.getEntity().getItemStack().containsEnchantment(Enchantment.DIG_SPEED)){
                event.getEntity().getItemStack().removeEnchantment(Enchantment.DIG_SPEED);
                return;
            }
            for(Entity e : event.getEntity().getNearbyEntities(0.2, 0.3, 0.2)){
                if(e instanceof Chicken && StackTools.hasSizeMoreThanOne(e)){
                    int stackSize = StackTools.getSize(e);
                    int dropAmount = (int) Math.round(stackSize * ((ThreadLocalRandom.current().nextDouble(0.5) + 0.35)));
                    sm.getDropTools().dropEggs(event.getEntity().getItemStack(), dropAmount, e.getLocation());
                    break;
                }
            }
        }
    }
}
