package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Turtle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.entity.StackTools;

import java.util.concurrent.ThreadLocalRandom;

public class ItemDrop implements Listener {

    private final IStackMob sm;

    public ItemDrop(IStackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onEggDrop(ItemSpawnEvent event) {
        switch (event.getEntity().getItemStack().getType()) {
            case EGG:
                // You might be wondering why, but it's to prevent the eggs being multiplied.
                // Metadata doesn't work because this event is fired before the metadata would be set.
                if (event.getEntity().getItemStack().containsEnchantment(Enchantment.DIG_SPEED)) {
                    event.getEntity().getItemStack().removeEnchantment(Enchantment.DIG_SPEED);
                    return;
                }
                for (Entity e : event.getEntity().getNearbyEntities(0.2, 0.3, 0.2)) {
                    if (e instanceof Chicken && StackTools.hasSizeMoreThanOne(e)) {
                        int stackSize = StackTools.getSize(e);
                        int dropAmount = (int) Math.round(stackSize * ((ThreadLocalRandom.current().nextDouble(0.5) + 0.35)));
                        sm.getDropTools().dropEggs(event.getEntity().getItemStack(), dropAmount, e.getLocation());
                        break;
                    }
                }
                break;
            case SCUTE:
                // You might be wondering why, but it's to prevent the eggs being multiplied.
                // Metadata doesn't work because this event is fired before the metadata would be set.
                if (event.getEntity().getItemStack().containsEnchantment(Enchantment.DIG_SPEED)) {
                    event.getEntity().getItemStack().removeEnchantment(Enchantment.DIG_SPEED);
                    return;
                }
                for (Entity e : event.getEntity().getNearbyEntities(1.0, 1.0, 1.0)) {
                    if (e instanceof Turtle && ((Turtle) e).getAge() == 0 && StackTools.hasSizeMoreThanOne(e)) {
                        int stackSize = StackTools.getSize(e);
                        int dropAmount = (int) Math.round(stackSize * ((ThreadLocalRandom.current().nextDouble(0.5) + 0.35)));
                        sm.getDropTools().dropEggs(event.getEntity().getItemStack(), dropAmount, e.getLocation());
                        break;
                    }
                }
                break;
        }
    }
}
