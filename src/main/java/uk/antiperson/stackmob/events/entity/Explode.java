package uk.antiperson.stackmob.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class Explode implements Listener {

    private StackMob sm;
    public Explode(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        if(event.getEntity().hasMetadata(GlobalValues.metaTag)){
            int stackSize = event.getEntity().getMetadata(GlobalValues.metaTag).get(0).asInt();
            event.setYield(stackSize * 2);
        }
    }
}
