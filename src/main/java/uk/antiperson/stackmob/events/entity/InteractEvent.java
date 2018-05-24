package uk.antiperson.stackmob.events.entity;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class InteractEvent implements Listener {

    private StackMob sm;

    public InteractEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if(!entity.hasMetadata(GlobalValues.METATAG)){
            return;
        }
        if(entity.hasMetadata(GlobalValues.CURRENTLY_BREEDING) && entity.getMetadata(GlobalValues.CURRENTLY_BREEDING).get(0).asBoolean()){
            return;
        }
        if(event.isCancelled()){
            return;
        }

        if(entity instanceof Animals){
            if(correctFood(event.getPlayer().getItemInHand(), entity) && ((Animals) entity).canBreed()){
                int stackSize = entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
                if(stackSize <= 1){
                    return;
                }

                if(sm.config.getCustomConfig().getBoolean("multiply.breed")){
                    int foodAmount = event.getPlayer().getItemInHand().getAmount();
                    if(foodAmount > stackSize){
                        foodAmount = stackSize;
                    }else{
                        Animals canBreed = (Animals) sm.tools.duplicate(entity, true);
                        canBreed.setBreed(true);
                        canBreed.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, stackSize - foodAmount));
                        canBreed.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
                        entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, foodAmount));
                    }

                    if(foodAmount > 1){
                        Animals small = (Animals) sm.tools.duplicate(entity,true);
                        small.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, (int) Math.floor(foodAmount / 2)));
                        small.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
                        small.setBaby();
                    }

                    ItemStack hand = event.getPlayer().getItemInHand();
                    hand.setAmount(hand.getAmount() - foodAmount);
                }else if(sm.config.getCustomConfig().getBoolean("divide-on.breed")) {
                    Entity newEntity = sm.tools.duplicate(entity, true);
                    newEntity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, stackSize - 1));
                    newEntity.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));

                    entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                    entity.setMetadata(GlobalValues.NO_STACK_ALL, new FixedMetadataValue(sm, true));
                    entity.setMetadata(GlobalValues.CURRENTLY_BREEDING, new FixedMetadataValue(sm, true));
                    entity.setCustomName(null);

                    // Allow to stack after breeding
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!entity.isDead()) {
                                entity.setMetadata(GlobalValues.CURRENTLY_BREEDING, new FixedMetadataValue(sm, false));
                                entity.setMetadata(GlobalValues.NO_STACK_ALL, new FixedMetadataValue(sm, false));
                            }
                        }
                    }.runTaskLater(sm, 20 * 20);
                }
                return;
            }
        }
        if(event.getPlayer().getItemInHand().getType() == Material.NAME_TAG && sm.config.getCustomConfig().getBoolean("divide-on.name")){
            if(entity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1){
                Entity dupe = sm.tools.duplicate(entity);
                dupe.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, entity.getMetadata(GlobalValues.METATAG).get(0).asInt() - 1));
                dupe.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
            }
            entity.removeMetadata(GlobalValues.METATAG, sm);
            entity.setMetadata(GlobalValues.NO_STACK_ALL, new FixedMetadataValue(sm, true));
        }
    }

    // There should be a method in bukkit for this...
    private boolean correctFood(ItemStack is, Entity entity){
        if((entity instanceof Cow || entity instanceof Sheep) && is.getType() == Material.WHEAT){
            return true;
        }
        if((entity instanceof Pig) && (is.getType() == Material.CARROT_ITEM || (sm.getVersionId() >= 2 && (is.getType() == Material.BEETROOT || is.getType() == Material.POTATO_ITEM)))){
            return true;
        }
        if((entity instanceof Chicken) && is.getType().toString().contains("SEED")){
            return true;
        }
        if(entity instanceof Horse && (is.getType() == Material.GOLDEN_APPLE || is.getType() == Material.GOLDEN_CARROT)){
            if(((Horse)entity).isTamed()){
                return true;
            }
        }
        if(entity instanceof Wolf && ((Wolf) entity).isTamed()){
            if (is.getType().toString().contains("RAW") || is.getType().toString().contains("COOKED") &&
                    !is.getType().toString().contains("FISH")) {
                return true;
            }
        }
        if(entity instanceof Ocelot && is.getType() == Material.RAW_FISH && ((Ocelot) entity).isTamed()){
            return true;
        }
        if(entity instanceof Rabbit && (is.getType() == Material.CARROT_ITEM || is.getType() == Material.GOLDEN_CARROT
                || is.getType() == Material.YELLOW_FLOWER)){
            return true;
        }
        if(sm.getVersionId() >= 4){
            return entity instanceof Llama && is.getType() == Material.HAY_BLOCK;
        }
        return false;
    }
}
