package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.checks.TraitManager;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class AgeableTrait implements ApplicableTrait {

    public AgeableTrait(TraitManager tc){
        if(tc.getStackMob().getCustomConfig().getBoolean("compare.entity-age")) {
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Ageable){
            return (((Ageable) original).isAdult() != ((Ageable) nearby).isAdult());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Ageable){
            ((Ageable) spawned).setAge(((Ageable) original).getAge());
        }
    }
}
