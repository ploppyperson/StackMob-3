package uk.antiperson.stackmob.listeners.entity;

import com.destroystokyo.paper.event.entity.EntityTransformedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.antiperson.stackmob.StackMob;

public class ConvertEvent implements Listener {

    private StackMob sm;
    public ConvertEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onConvert(EntityTransformedEvent event){
        if(event.getReason() != EntityTransformedEvent.TransformedReason.DROWNED){
            return;
        }
        if(sm.getStackTools().hasValidStackData(event.getEntity())){
            int stackSize = sm.getStackTools().getSize(event.getEntity());
            sm.getStackTools().setSize(event.getTransformed(), stackSize);
            sm.getStackTools().removeSize(event.getEntity());
        }
    }
}
