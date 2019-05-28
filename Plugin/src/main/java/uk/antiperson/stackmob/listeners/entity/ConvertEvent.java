package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import uk.antiperson.stackmob.api.entity.StackTools;

public class ConvertEvent implements Listener {

    @EventHandler
    public void onConvert(EntityTransformEvent event){
        if(event.getTransformReason() != EntityTransformEvent.TransformReason.DROWNED){
            return;
        }
        if(StackTools.hasValidStackData(event.getEntity())){
            int stackSize = StackTools.getSize(event.getEntity());
            StackTools.setSize(event.getTransformedEntity(), stackSize);
            StackTools.removeSize(event.getEntity());
        }
    }
}
