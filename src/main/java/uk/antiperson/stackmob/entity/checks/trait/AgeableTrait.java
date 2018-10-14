package uk.antiperson.stackmob.entity.checks.trait;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.entity.checks.ApplicableTrait;
import uk.antiperson.stackmob.entity.checks.TraitChecks;

public class AgeableTrait implements ApplicableTrait {

    public AgeableTrait(TraitChecks tc){
        if(tc.getStackMob().config.getCustomConfig().getBoolean("compare.entity-age")) {
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
