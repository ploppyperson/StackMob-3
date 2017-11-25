package uk.antiperson.stackmob.events.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class Breed implements Listener {

    private StackMob sm;

    public Breed(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {
        LivingEntity father = event.getFather();
        LivingEntity mother = event.getMother();
        if(father.hasMetadata(GlobalValues.CURRENTLY_BREEDING) && father.getMetadata(GlobalValues.CURRENTLY_BREEDING).get(0).asBoolean()){
            father.setMetadata(GlobalValues.CURRENTLY_BREEDING, new FixedMetadataValue(sm, false));
            father.setMetadata(GlobalValues.NO_STACK_ALL, new FixedMetadataValue(sm, false));
        }
        if(mother.hasMetadata(GlobalValues.CURRENTLY_BREEDING) && mother.getMetadata(GlobalValues.CURRENTLY_BREEDING).get(0).asBoolean()){
            mother.setMetadata(GlobalValues.CURRENTLY_BREEDING, new FixedMetadataValue(sm, false));
            mother.setMetadata(GlobalValues.NO_STACK_ALL, new FixedMetadataValue(sm, false));
        }
    }
}
