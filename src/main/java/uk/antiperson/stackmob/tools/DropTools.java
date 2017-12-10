package uk.antiperson.stackmob.tools;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class to calculate the correct amount of entity drops.
 */
public class DropTools {

    private StackMob sm;
    public DropTools(StackMob sm){
        this.sm = sm;
    }

    public void calculateDrops(List<ItemStack> drops, int multiplier, Location dropLocation, ItemStack itemInHand){
        for(ItemStack itemStack : drops){
            if(!sm.config.getCustomConfig().getStringList("multiply-drops.drops-whitelist")
                    .contains(itemStack.getType().toString())){
                continue;
            }
            if(sm.config.getCustomConfig().getStringList("multiply-drops.drop-one-per")
                    .contains(itemStack.getType().toString())){
                dropDrops(itemStack, multiplier, dropLocation);
                continue;
            }
            if(itemInHand != null && itemInHand.getEnchantments().containsKey(Enchantment.LOOT_BONUS_MOBS)) {
                double enchantmentTimes = 1 + itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) * 0.5;
                dropDrops(itemStack, Math.round(calculateAmount(multiplier) * enchantmentTimes), dropLocation);
                continue;
            }
            dropDrops(itemStack, Math.round(calculateAmount(multiplier)), dropLocation);
        }
    }

    // Calculate a random drop amount.
    public double calculateAmount(int mutiplier){
        return (0.75 + ThreadLocalRandom.current().nextDouble(2)) * mutiplier;
    }

    // Method to drop the correct amount of drops.
    public void dropDrops(ItemStack drop, double amount, Location dropLocation){
        double inStacks = amount / drop.getMaxStackSize();
        double floor = Math.floor(inStacks);
        double leftOver = inStacks - floor;
        for(int i = 1; i <= floor; i++){
            ItemStack newStack = new ItemStack(drop.getType(), drop.getMaxStackSize(), drop.getDurability());
            newStack.setItemMeta(drop.getItemMeta());
            dropLocation.getWorld().dropItemNaturally(dropLocation, newStack);
        }
        if(leftOver > 0){
            ItemStack newStack = new ItemStack(drop.getType(), (int) Math.round(leftOver * drop.getMaxStackSize()), drop.getDurability());
            newStack.setItemMeta(drop.getItemMeta());
            dropLocation.getWorld().dropItemNaturally(dropLocation, newStack);
        }
    }
}
