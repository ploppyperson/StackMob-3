package uk.antiperson.stackmob.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class ExplodeEvent implements Listener {

    private StackMob sm;
    public ExplodeEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        if(event.getEntity().hasMetadata(GlobalValues.METATAG)){
            int stackSize = event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt();
            event.setYield(stackSize * 2);
        }
    }
}
