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
                dropDrops(itemStack, (int) Math.round(calculateAmount(multiplier) * enchantmentTimes), dropLocation);
                continue;
            }
            dropDrops(itemStack, calculateAmount(multiplier), dropLocation);
        }
    }

    // Calculate a random drop amount.
    public int calculateAmount(int mutiplier){
        return (int) Math.round((0.75 + ThreadLocalRandom.current().nextDouble(2)) * mutiplier);
    }

    public void dropDrops(ItemStack drop, int amount, Location dropLocation){
        dropAllDrops(drop, amount, dropLocation, false);
    }

    public void dropEggs(ItemStack drop, int amount, Location dropLocation){
        dropAllDrops(drop, amount, dropLocation, true);
    }

    // Method to drop the correct amount of drops.
    private void dropAllDrops(ItemStack drop, int amount, Location dropLocation, boolean addEnchantment){
        double inStacks = (double) amount / (double) drop.getMaxStackSize();
        double floor = Math.floor(inStacks);
        double leftOver = inStacks - floor;
        for(int i = 1; i <= floor; i++){
            ItemStack newStack = new ItemStack(drop.getType(), drop.getMaxStackSize(), drop.getDurability());
            newStack.setItemMeta(drop.getItemMeta());
            if(addEnchantment){
                newStack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
            }
            dropLocation.getWorld().dropItemNaturally(dropLocation, newStack);
        }
        if(leftOver > 0){
            ItemStack newStack = new ItemStack(drop.getType(), (int) Math.round(leftOver * drop.getMaxStackSize()), drop.getDurability());
            sm.getLogger().info(newStack.getAmount() + "");
            newStack.setItemMeta(drop.getItemMeta());
            if(addEnchantment){
                newStack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
            }
            dropLocation.getWorld().dropItemNaturally(dropLocation, newStack);
        }
    }

}
