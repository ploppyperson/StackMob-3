package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class TameEvent implements Listener {

    private StackMob sm;

    public TameEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onTame(EntityTameEvent event) {
        if(GeneralTools.hasValidStackData(event.getEntity())){
            if(event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt() > 1){
                Entity dupe = sm.tools.duplicate(event.getEntity());
                dupe.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, event.getEntity().getMetadata(GlobalValues.METATAG).get(0).asInt() - 1));
                dupe.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
            }
            event.getEntity().removeMetadata(GlobalValues.METATAG, sm);
            event.getEntity().setMetadata(GlobalValues.NO_STACK_ALL, new FixedMetadataValue(sm, true));
            event.getEntity().setCustomName(null);
            event.getEntity().setCustomNameVisible(false);
        }
    }
}
