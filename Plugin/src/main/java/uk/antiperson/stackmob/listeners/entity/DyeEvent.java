package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import uk.antiperson.stackmob.api.StackMob;
import uk.antiperson.stackmob.api.entity.StackTools;

public class DyeEvent implements Listener {

    private StackMob sm;
    public DyeEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onSheepDye(SheepDyeWoolEvent event) {
        if(!(StackTools.hasSizeMoreThanOne(event.getEntity()))) {
            return;
        }
        if(event.isCancelled()){
            return;
        }
        int stackSize = StackTools.getSize(event.getEntity());
        Sheep oldEntity = event.getEntity();

        if (sm.getCustomConfig().getBoolean("divide-on.sheep-dye")) {
            Sheep newEntity = (Sheep) sm.getTools().duplicate(oldEntity);
            newEntity.setColor(event.getEntity().getColor());
            oldEntity.setColor(event.getColor());
            StackTools.setSize(newEntity,stackSize - 1);
            StackTools.makeSingle(oldEntity);
        }
    }
}
