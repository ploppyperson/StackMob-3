package uk.antiperson.stackmob.api.bcompat;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface Compat {

    void onEnable();

    boolean checkFood(Entity entity, ItemStack food);
}
