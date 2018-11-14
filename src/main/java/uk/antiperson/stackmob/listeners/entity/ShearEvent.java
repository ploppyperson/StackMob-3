package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootContext;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;

import java.util.Collection;
import java.util.Random;

public class ShearEvent implements Listener {

    private StackMob sm;
    public ShearEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onSheepShear(PlayerShearEntityEvent event) {
        if(!(StackTools.hasValidStackData(event.getEntity()))){
            return;
        }
        if(event.isCancelled()){
            return;
        }

        Entity oldEntity = event.getEntity();
        int stackSize = StackTools.getSize(oldEntity);
        if(oldEntity instanceof Sheep){
            Sheep oldSheep = (Sheep) oldEntity;
            if(sm.getCustomConfig().getBoolean("multiply.sheep-wool")){
                LootContext lootContext = new LootContext.Builder(oldSheep.getLocation()).lootedEntity(oldSheep).build();
                Collection<ItemStack> loot = oldSheep.getLootTable().populateLoot(new Random(), lootContext);
                for(ItemStack itemStack : loot){
                    if(Tag.WOOL.isTagged(itemStack.getType())){
                        sm.dropTools.dropDrops(itemStack, sm.dropTools.calculateAmount(stackSize), oldEntity.getLocation());
                    }
                }

                damageItemInHand(event.getPlayer(), stackSize);
            }else if(sm.getCustomConfig().getBoolean("divide-on.sheep-shear")){
                Sheep newEntity = (Sheep) sm.tools.duplicate(oldEntity);
                newEntity.setSheared(false);

                StackTools.setSize(newEntity,stackSize - 1);
                StackTools.setSize(oldEntity, 1);
                oldEntity.setCustomName(null);
            }
        }

        if(oldEntity instanceof MushroomCow){
            if(sm.getCustomConfig().getBoolean("multiply.mooshroom-mushrooms")){
                // Duplicate mushrooms
                ItemStack mushrooms = new ItemStack(Material.RED_MUSHROOM,1);
                sm.dropTools.dropDrops(mushrooms, (stackSize - 1) * 5, oldEntity.getLocation());

                // Spawn separate normal cow for the rest of the stack.
                Entity cow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.COW);
                StackTools.setSize(cow,stackSize - 1);
                // Set the required damage as if done separately
                damageItemInHand(event.getPlayer(), stackSize);
            }else if (sm.getCustomConfig().getBoolean("divide-on.mooshroom-shear")){
                Entity mushroomCow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.MUSHROOM_COW);
                StackTools.setSize(mushroomCow,stackSize - 1);
                oldEntity.setCustomName(null);
            }
        }
    }


    private void damageItemInHand(Player player, int stackSize){
        ItemStack item = player.getInventory().getItemInMainHand();
        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(meta.getDamage() + stackSize);
        item.setItemMeta((ItemMeta) meta);
        player.getInventory().setItemInMainHand(item);
    }
}
