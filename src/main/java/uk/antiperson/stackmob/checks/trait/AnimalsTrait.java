package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitManager;

public class AnimalsTrait implements ApplicableTrait {

    public AnimalsTrait(TraitManager tc){
        if(tc.getStackMob().config.getCustomConfig().getBoolean("compare.can-breed")){
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Animals){
            return (((Animals) original).canBreed() != ((Animals) nearby).canBreed());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Animals){
            ((Animals) spawned).setBreed(((Animals) original).canBreed());
        }
    }
}
