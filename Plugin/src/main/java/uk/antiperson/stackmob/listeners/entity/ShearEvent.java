package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.material.Wool;
import uk.antiperson.stackmob.api.StackMob;
import uk.antiperson.stackmob.api.entity.StackTools;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class ShearEvent implements Listener {

    private StackMob sm;
    public ShearEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onSheepShear(PlayerShearEntityEvent event) {
        if(!(StackTools.hasSizeMoreThanOne(event.getEntity()))){
            return;
        }
        if(event.isCancelled()){
            return;
        }

        Entity oldEntity = event.getEntity();
        int stackSize = StackTools.getSize(oldEntity);
        if(oldEntity instanceof Sheep){
            Sheep oldSheep = (Sheep) oldEntity;
            if(sm.getCustomConfig().getBoolean("multiply.sheep-wool") && hasEnoughDurability(event.getPlayer(), stackSize)){
                LootContext lootContext = new LootContext.Builder(oldSheep.getLocation()).lootedEntity(oldSheep).build();
                Collection<ItemStack> loot = oldSheep.getLootTable().populateLoot(ThreadLocalRandom.current(), lootContext);
                for(ItemStack itemStack : loot){
                    if(itemStack.getData() instanceof Wool) {
                        sm.getDropTools().dropDrops(itemStack, sm.getDropTools().calculateAmount(stackSize), oldEntity.getLocation());
                    }
                }

                damageItemInHand(event.getPlayer(), stackSize);
            }else if(sm.getCustomConfig().getBoolean("divide-on.sheep-shear")){
                Sheep newEntity = (Sheep) sm.getTools().duplicate(oldEntity);
                newEntity.setSheared(false);

                StackTools.setSize(newEntity,stackSize - 1);
                StackTools.makeSingle(oldEntity);
            }
        }

        if(oldEntity instanceof MushroomCow){
            if(sm.getCustomConfig().getBoolean("multiply.mooshroom-mushrooms") && hasEnoughDurability(event.getPlayer(), stackSize)){
                // Duplicate mushrooms
                ItemStack mushrooms = new ItemStack(Material.RED_MUSHROOM,1);
                sm.getDropTools().dropDrops(mushrooms, (stackSize - 1) * 5, oldEntity.getLocation());

                // Spawn separate normal cow for the rest of the stack.
                Entity cow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.COW);
                StackTools.setSize(cow,stackSize - 1);
                // Set the required damage as if done separately
                damageItemInHand(event.getPlayer(), stackSize);
                StackTools.removeSize(oldEntity);
            }else if (sm.getCustomConfig().getBoolean("divide-on.mooshroom-shear")){
                Entity mushroomCow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.MUSHROOM_COW);
                StackTools.setSize(mushroomCow,stackSize - 1);
                StackTools.makeSingle(oldEntity);
            }
        }
    }


    private void damageItemInHand(Player player, int stackSize){
        ItemStack item = player.getInventory().getItemInMainHand();
        Damageable meta = (Damageable) item.getItemMeta();
        int newDamage = meta.getDamage() + stackSize;
        if(newDamage <= item.getType().getMaxDurability()){
            meta.setDamage(meta.getDamage() + stackSize);
            item.setItemMeta((ItemMeta) meta);
            player.getInventory().setItemInMainHand(item);
            return;
        }
        player.getInventory().setItemInMainHand(null);
    }

    private boolean hasEnoughDurability(Player player, int stackSize) {
        ItemStack item = player.getInventory().getItemInMainHand();
        Damageable meta = (Damageable) item.getItemMeta();
        int newDamage = meta.getDamage() + stackSize;
        if(newDamage >= item.getType().getMaxDurability()){
            player.sendActionBar(ChatColor.RED + "Item is too damaged to shear all!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            return false;
        }
        return true;
    }
}
