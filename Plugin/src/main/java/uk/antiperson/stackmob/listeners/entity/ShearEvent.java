package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.api.StackMob;
import uk.antiperson.stackmob.api.entity.StackTools;
import uk.antiperson.stackmob.api.tools.ItemTools;

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
            Sheep sheep = (Sheep) oldEntity;
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if(sm.getLogic().doSheepShearAll(sheep, item)){
                ItemTools.damageItemInHand(event.getPlayer(), stackSize);
            }else{
                sm.getLogic().doSheepShearSingle(sheep);
            }
        }

        // TODO: Make method in stacklogic class for this as well.
        if(oldEntity instanceof MushroomCow){
            if(sm.getCustomConfig().getBoolean("multiply.mooshroom-mushrooms") && ItemTools.hasEnoughDurability(event.getPlayer(), stackSize)){
                // Duplicate mushrooms
                ItemStack mushrooms = new ItemStack(Material.RED_MUSHROOM,1);
                sm.getDropTools().dropDrops(mushrooms, (stackSize - 1) * 5, oldEntity.getLocation());

                // Spawn separate normal cow for the rest of the stack.
                Entity cow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.COW);
                StackTools.setSize(cow,stackSize - 1);
                // Set the required damage as if done separately
                ItemTools.damageItemInHand(event.getPlayer(), stackSize);
                StackTools.removeSize(oldEntity);
            }else if (sm.getCustomConfig().getBoolean("divide-on.mooshroom-shear")){
                Entity mushroomCow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.MUSHROOM_COW);
                StackTools.setSize(mushroomCow,stackSize - 1);
                StackTools.makeSingle(oldEntity);
            }
        }
    }

}
