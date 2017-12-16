package uk.antiperson.stackmob.events.entity;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class ShearEvent implements Listener {

    private StackMob sm;

    public ShearEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onSheepShear(PlayerShearEntityEvent event) {
        if(!event.getEntity().hasMetadata(GlobalValues.METATAG)){
            return;
        }
        if(event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt() <= 1){
            return;
        }

        Entity oldEntity = event.getEntity();
        int stackSize = event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt();

        if(oldEntity instanceof Sheep){
            Sheep oldSheep = (Sheep) oldEntity;
            if(sm.config.getCustomConfig().getBoolean("multiply.sheep-wool")){
                Wool wool = new Wool(oldSheep.getColor());
                sm.dropTools.dropDrops(wool.toItemStack(1), sm.dropTools.calculateAmount(stackSize), oldEntity.getLocation());

                ItemStack item = event.getPlayer().getItemInHand();
                item.setDurability((short) (item.getDurability() + stackSize));
                event.getPlayer().setItemInHand(item);

            }else if(sm.config.getCustomConfig().getBoolean("divide-on.sheep-shear")){
                Sheep newEntity = (Sheep) sm.tools.duplicate(oldEntity);
                newEntity.setSheared(false);

                newEntity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, stackSize - 1));
                newEntity.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
                oldEntity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                oldEntity.setCustomName(null);
            }
        }

        if(oldEntity instanceof MushroomCow){
            if(sm.config.getCustomConfig().getBoolean("multiply.mooshroom-mushrooms")){
                ItemStack mushrooms = new ItemStack(Material.RED_MUSHROOM,1);
                sm.dropTools.dropDrops(mushrooms, (stackSize - 1) * 5, oldEntity.getLocation());
                // SpawnEvent cow for the rest of the cows that where sheared.
                Entity cow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.COW);
                cow.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, stackSize - 1));
                cow.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
                ItemStack item = event.getPlayer().getItemInHand();
                item.setDurability((short) (item.getDurability() + stackSize));
                event.getPlayer().setItemInHand(item);
            }else if (sm.config.getCustomConfig().getBoolean("divide-on.mooshroom-shear")){
                Entity mushroomCow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.MUSHROOM_COW);
                mushroomCow.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, stackSize - 1));
                mushroomCow.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
                oldEntity.setCustomName(null);
            }
        }
    }
}
