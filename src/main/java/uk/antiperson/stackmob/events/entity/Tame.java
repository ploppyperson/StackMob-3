package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import javax.xml.bind.annotation.XmlElementDecl;

public class Tame implements Listener {

    private StackMob sm;

    public Tame(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onTame(EntityTameEvent event) {
        if(event.getEntity().hasMetadata(GlobalValues.metaTag)){
            if(event.getEntity().getMetadata(GlobalValues.metaTag).get(0).asInt() > 1){
                Entity dupe = sm.checks.duplicate(event.getEntity());
                dupe.setMetadata(GlobalValues.metaTag, new FixedMetadataValue(sm, event.getEntity().getMetadata(GlobalValues.metaTag).get(0).asInt() - 1));
                dupe.setMetadata(GlobalValues.noSpawnStack, new FixedMetadataValue(sm, true));
            }
            event.getEntity().setMetadata(GlobalValues.metaTag, new FixedMetadataValue(sm, 0));
            event.getEntity().setMetadata(GlobalValues.noStackAll, new FixedMetadataValue(sm, true));
            event.getEntity().setCustomName(null);
            event.getEntity().setCustomNameVisible(false);
        }
    }
}
