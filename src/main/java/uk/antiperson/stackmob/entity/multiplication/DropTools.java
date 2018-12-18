package uk.antiperson.stackmob.entity.multiplication;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.hooks.CustomDropsHook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class to calculate the correct amount of entity drops.
 */
public class DropTools {

    private StackMob sm;
    public DropTools(StackMob sm){
        this.sm = sm;
    }

    public void dropDrops(int deadAmount, LivingEntity dead){
        if(deadAmount > sm.getCustomConfig().getInt("multiply-drops.entity-limit")){
            deadAmount = sm.getCustomConfig().getInt("multiply-drops.entity-limit");
        }
        Map<ItemStack, Integer> rawDrops = calculateDrops(deadAmount, dead);
        rawDrops.forEach((item, amount) -> dropDrops(item, amount, dead.getLocation()));
    }

    public Map<ItemStack, Integer> calculateDrops(int deadAmount, LivingEntity dead){
        Map<ItemStack, Integer> drops = new HashMap<>();
        for(int i = 0; i < deadAmount; i++){
            for(ItemStack stack : generateLoot(dead)){
                if(stack == null || stack.getType() == Material.AIR){
                    continue;
                }
                if(sm.getCustomConfig().getStringList("multiply-drops.drops-blacklist")
                        .contains(stack.getType().toString())){
                    continue;
                }
                if(sm.getCustomConfig().getStringList("multiply-drops.drop-one-per")
                        .contains(stack.getType().toString())){
                    stack.setAmount(1);
                }
                for(ItemStack itemStack : drops.keySet()){
                    if(itemStack.isSimilar(stack)){
                        drops.put(itemStack, drops.get(itemStack) + stack.getAmount());
                        break;
                    }
                }
                if(!drops.containsKey(stack)) {
                    drops.put(stack, stack.getAmount());
                }
            }
        }
        return drops;
    }

    public Collection<ItemStack> generateLoot(LivingEntity dead){
        if(sm.getHookManager().isHookRegistered(PluginCompat.CUSTOMDROPS)){
            CustomDropsHook cdh = (CustomDropsHook) sm.getHookManager().getHook(PluginCompat.CUSTOMDROPS);
            if(cdh.hasCustomDrops(dead)){
                return cdh.getDrops(dead);
            }
        }
        LootContext lootContext = new LootContext.Builder(dead.getLocation()).lootedEntity(dead).killer(dead.getKiller()).build();
        Collection<ItemStack> items = ((Mob) dead).getLootTable().populateLoot(new Random(), lootContext);
        return items;
    }

    // Calculate a random drop amount.
    public int calculateAmount(int multiplier){
        return (int) Math.round((0.75 + ThreadLocalRandom.current().nextDouble(2)) * multiplier);
    }

    public void dropEggs(ItemStack drop, int amount, Location dropLocation){
        Collection<ItemStack> drops = convertToStacks(drop, amount);
        drops.forEach(itemStack -> itemStack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1));
        dropStacks(drops, dropLocation);
    }

    public void dropDrops(ItemStack drop, int amount, Location dropLocation){
        Collection<ItemStack> drops = convertToStacks(drop, amount);
        dropStacks(drops, dropLocation);
    }

    private void dropStacks(Collection<ItemStack> drops, Location location){
        drops.forEach(itemStack -> location.getWorld().dropItemNaturally(location, itemStack));
    }

    // Method to create itemstacks.
    private Collection<ItemStack> convertToStacks(ItemStack drop, int amount){
        List<ItemStack> items = new ArrayList<>();
        double inStacks = (double) amount / (double) drop.getMaxStackSize();
        double floor = Math.floor(inStacks);
        double leftOver = inStacks - floor;
        for(int i = 0; i < floor; i++){
            ItemStack newStack = drop.clone();
            newStack.setAmount(drop.getMaxStackSize());
            items.add(newStack);
        }
        if(leftOver > 0){
            ItemStack newStack = drop.clone();
            newStack.setAmount((int) Math.round(leftOver * drop.getMaxStackSize()));
            items.add(newStack);
        }
        return items;
    }

    private boolean dropIsArmor(LivingEntity entity, ItemStack drop){
        if(entity.getEquipment().getItemInMainHand().equals(drop) || entity.getEquipment().getItemInOffHand().equals(drop)){
            return true;
        }
        for(ItemStack itemStack : entity.getEquipment().getArmorContents()){
            if(itemStack.equals(drop)){
                return true;
            }
        }
        return false;
    }


}
