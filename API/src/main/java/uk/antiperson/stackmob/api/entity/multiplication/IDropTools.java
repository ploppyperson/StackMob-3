package uk.antiperson.stackmob.api.entity.multiplication;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface IDropTools {
    void doDrops(int deadAmount, LivingEntity dead);

    // Calculate a random drop amount.
    int calculateAmount(int multiplier);

    void dropEggs(ItemStack drop, int amount, Location dropLocation);

    void dropDrops(ItemStack drop, int amount, Location dropLocation);
}
