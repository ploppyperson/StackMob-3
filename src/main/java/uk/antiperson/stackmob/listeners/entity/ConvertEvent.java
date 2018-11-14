package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import uk.antiperson.stackmob.StackMob;

public class ConvertEvent implements Listener {

    private StackMob sm;
    public ConvertEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onConvert(EntityTransformEvent event){
        if(event.getTransformReason() != EntityTransformEvent.TransformReason.DROWNED){
            return;
        }
        if(sm.getStackTools().hasValidStackData(event.getEntity())){
            int stackSize = sm.getStackTools().getSize(event.getEntity());
            sm.getStackTools().setSize(event.getTransformedEntity(), stackSize);
            sm.getStackTools().removeSize(event.getEntity());
        }
    }
}
