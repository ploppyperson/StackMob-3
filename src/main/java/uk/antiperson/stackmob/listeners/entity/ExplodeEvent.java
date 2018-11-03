package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import uk.antiperson.stackmob.StackMob;

public class ExplodeEvent implements Listener {

    private StackMob sm;
    public ExplodeEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        if(sm.getStackTools().hasValidStackData(event.getEntity())){
            int stackSize = sm.getStackTools().getSize(event.getEntity());
            event.setYield(event.getYield() + (event.getYield() * (stackSize - 1) * 0.5f));
        }
    }
}
