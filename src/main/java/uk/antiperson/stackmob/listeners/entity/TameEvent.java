package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import uk.antiperson.stackmob.StackMob;

public class TameEvent implements Listener {

    private StackMob sm;

    public TameEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onTame(EntityTameEvent event) {
        Entity entity = event.getEntity();
        if(sm.getStackTools().hasValidStackData(entity)){
            int stackSize = sm.getStackTools().getSize(entity);
            if(stackSize > 1){
                Entity dupe = sm.tools.duplicate(entity);
                sm.getStackTools().setSize(dupe, stackSize - 1);
            }
            sm.getStackTools().removeSize(entity);
        }
    }
}
