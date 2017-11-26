package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.Config;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.DropService;
import uk.antiperson.stackmob.services.EntityService;

@AllArgsConstructor
public class ShearListener implements Listener {

    private Config config;
    private DropService dropService;
    private EntityService entityService;
    private BukkitService bukkitService;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSheepShear(PlayerShearEntityEvent event) {
        if (!event.getEntity().hasMetadata(GlobalValues.METATAG)) {
            return;
        }
        if (event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt() <= 1) {
            return;
        }

        Entity oldEntity = event.getEntity();
        int stackSize = event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt();

        if (oldEntity instanceof Sheep) {
            Sheep oldSheep = (Sheep) oldEntity;
            if (config.get().getBoolean("multiply.sheep-wool")) {
                Wool wool = new Wool(oldSheep.getColor());
                dropService.dropDrops(wool.toItemStack(1), dropService.calculateAmount(stackSize), oldEntity.getLocation());

                ItemStack item = event.getPlayer().getItemInHand();
                item.setDurability((short) (item.getDurability() + stackSize));
                event.getPlayer().setItemInHand(item);

            } else if (config.get().getBoolean("divide-on.sheep-shear")) {
                Sheep newEntity = (Sheep) entityService.duplicate(oldEntity);
                newEntity.setSheared(false);
                bukkitService.setMetadata(newEntity, GlobalValues.METATAG, stackSize - 1);
                bukkitService.setMetadata(newEntity, GlobalValues.NO_SPAWN_STACK, true);
                bukkitService.setMetadata(oldEntity, GlobalValues.METATAG, 1);
                oldEntity.setCustomName(null);
            }
        }

        if (oldEntity instanceof MushroomCow) {
            if (config.get().getBoolean("multiply.mooshroom-mushrooms")) {
                ItemStack mushrooms = new ItemStack(Material.RED_MUSHROOM, 1);
                dropService.dropDrops(mushrooms, (stackSize - 1) * 5, oldEntity.getLocation());
                // Spawn cow for the rest of the cows that where sheared.
                Entity cow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.COW);
                bukkitService.setMetadata(cow, GlobalValues.METATAG, stackSize - 1);
                bukkitService.setMetadata(cow, GlobalValues.NO_SPAWN_STACK, true);
                ItemStack item = event.getPlayer().getItemInHand();
                item.setDurability((short) (item.getDurability() + stackSize));
                event.getPlayer().setItemInHand(item);
            } else if (config.get().getBoolean("divide-on.mooshroom-shear")) {
                Entity mushroomCow = oldEntity.getWorld().spawnEntity(oldEntity.getLocation(), EntityType.MUSHROOM_COW);
                bukkitService.setMetadata(mushroomCow, GlobalValues.METATAG, stackSize - 1);
                bukkitService.setMetadata(mushroomCow, GlobalValues.NO_SPAWN_STACK, true);
                oldEntity.setCustomName(null);
            }
        }
    }

}
