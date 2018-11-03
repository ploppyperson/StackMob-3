package uk.antiperson.stackmob.listeners.entity;

import com.destroystokyo.paper.event.entity.EntityTransformedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

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

            event.getTransformed().setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
            sm.getStackTools().setSize(event.getTransformed(), stackSize);

            sm.getStackTools().removeSize(event.getEntity());
        }
    }
}
