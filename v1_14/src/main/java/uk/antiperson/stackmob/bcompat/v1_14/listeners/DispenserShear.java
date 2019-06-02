package uk.antiperson.stackmob.bcompat.v1_14.listeners;

import org.bukkit.block.Container;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.entity.StackTools;
import uk.antiperson.stackmob.api.tools.ItemTools;

public class DispenserShear implements Listener {

    private IStackMob sm;
    public DispenserShear(IStackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onDispenserShear(BlockShearEntityEvent event) {
        if(!StackTools.hasSizeMoreThanOne(event.getEntity())) {
            return;
        }
        if(event.getEntity() instanceof Sheep) {
            Sheep sheep = (Sheep) event.getEntity();
            if(sm.getLogic().doSheepShearAll(sheep, event.getTool())){
                ItemStack is = ItemTools.damageItem(event.getTool(), StackTools.getSize(event.getEntity()));
                sm.getServer().getScheduler().runTask(sm, () -> {
                    Container dispenser = (Container) event.getBlock().getState();
                    int itemId = dispenser.getInventory().first(event.getTool());
                    dispenser.getInventory().setItem(itemId, is);
                });
                return;
            }
            sm.getLogic().doSheepShearSingle(sheep);
        } else {
            throw new UnsupportedOperationException("This is not implemented!");
        }
    }
}
