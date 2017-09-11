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
        if(father.hasMetadata(GlobalValues.curentlyBreeding) && father.getMetadata(GlobalValues.curentlyBreeding).get(0).asBoolean()){
            father.setMetadata(GlobalValues.curentlyBreeding, new FixedMetadataValue(sm, false));
            father.setMetadata(GlobalValues.noStackAll, new FixedMetadataValue(sm, false));
        }
        if(mother.hasMetadata(GlobalValues.curentlyBreeding) && mother.getMetadata(GlobalValues.curentlyBreeding).get(0).asBoolean()){
            mother.setMetadata(GlobalValues.curentlyBreeding, new FixedMetadataValue(sm, false));
            mother.setMetadata(GlobalValues.noStackAll, new FixedMetadataValue(sm, false));
        }
    }
}
