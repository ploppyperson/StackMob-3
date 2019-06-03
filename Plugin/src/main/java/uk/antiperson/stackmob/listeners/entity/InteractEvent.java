package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.entity.StackTools;

public class InteractEvent implements Listener {

    private IStackMob sm;
    public InteractEvent(IStackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if(!(StackTools.hasValidData(entity))){
            return;
        }
        if(event.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }
        if(event.isCancelled()){
            return;
        }

        int stackSize = StackTools.getSize(entity);
        if(entity instanceof Animals){
            Animals animals = (Animals) entity;
            if(sm.getCompat().checkFood(entity, event.getPlayer().getInventory().getItemInMainHand()) && animals.canBreed()){
                if(StackTools.hasSizeMoreThanOne(entity)) {
                    if (sm.getCustomConfig().getBoolean("multiply.breed")) {
                        int breedSize = stackSize;
                        int handSize = event.getPlayer().getInventory().getItemInMainHand().getAmount();
                        if (handSize < breedSize) {
                            breedSize = event.getPlayer().getInventory().getItemInMainHand().getAmount();
                            event.getPlayer().getInventory().setItemInMainHand(null);
                        }

                        int childAmount = breedSize / 2;
                        Animals child = (Animals) sm.getTools().duplicate(entity);
                        StackTools.setSize(child, childAmount);
                        child.setBaby();

                        event.getPlayer().getInventory().getItemInMainHand().setAmount(handSize - breedSize);
                        animals.setBreed(false);
                    } else if (sm.getCustomConfig().getBoolean("divide-on.breed")) {
                        Entity newEntity = sm.getTools().duplicate(entity);
                        StackTools.setSize(newEntity,stackSize - 1);
                        StackTools.makeSingle(entity);
                    }
                    return;
                }
            }
        }
        if(sm.getCustomConfig().getBoolean("divide-on.name")) {
            ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
            if (handItem.getType() == Material.NAME_TAG && handItem.getItemMeta().hasDisplayName()) {
                if (stackSize > 1) {
                    Entity dupe = sm.getTools().duplicate(entity);
                    StackTools.setSize(dupe,stackSize - 1);
                }
                StackTools.removeSize(entity);
            }
        }
    }

}
