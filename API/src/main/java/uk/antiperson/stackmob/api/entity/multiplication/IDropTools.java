package uk.antiperson.stackmob.api.entity.multiplication;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IDropTools {
    void doDrops(int deadAmount, LivingEntity dead, List<ItemStack> drops);

    // Calculate a random drop amount.
    int calculateAmount(int multiplier);

    void dropEggs(ItemStack drop, int amount, Location dropLocation);

    void dropDrops(ItemStack drop, int amount, Location dropLocation);
}
