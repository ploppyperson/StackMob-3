package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import uk.antiperson.stackmob.api.entity.StackTools;

public class ExplodeEvent implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        if(StackTools.hasSizeMoreThanOne(event.getEntity())){
            int stackSize = StackTools.getSize(event.getEntity());
            event.setYield(event.getYield() + (event.getYield() * (stackSize - 1) * 0.5f));
        }
    }
}
