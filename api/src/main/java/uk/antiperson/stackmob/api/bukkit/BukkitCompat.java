package uk.antiperson.stackmob.api.bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface BukkitCompat {

    boolean checkFood(ItemStack is, Entity entity);
}
