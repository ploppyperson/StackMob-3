package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GlobalValues;

public class BreedEvent implements Listener {

    private StackMob sm;

    public BreedEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {
        LivingEntity father = event.getFather();
        LivingEntity mother = event.getMother();
        if(father.hasMetadata(GlobalValues.BREED_MODE)){
            father.setMetadata(GlobalValues.BREED_MODE, new FixedMetadataValue(sm, false));
            father.setMetadata(GlobalValues.NO_STACK, new FixedMetadataValue(sm, false));
        }
        if(mother.hasMetadata(GlobalValues.BREED_MODE)){
            mother.setMetadata(GlobalValues.BREED_MODE, new FixedMetadataValue(sm, false));
            mother.setMetadata(GlobalValues.NO_STACK, new FixedMetadataValue(sm, false));
        }
    }
}
