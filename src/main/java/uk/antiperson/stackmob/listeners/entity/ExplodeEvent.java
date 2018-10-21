package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class ExplodeEvent implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event){
        if(GeneralTools.hasValidStackData(event.getEntity())){
            int stackSize = event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt();
            event.setYield(event.getYield() + (event.getYield() * (stackSize - 1) * 0.5f));
        }
    }
}
