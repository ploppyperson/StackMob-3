package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitManager;

public class TameableTrait implements ApplicableTrait {

    public TameableTrait(TraitManager tc){
        if(tc.getStackMob().getCustomConfig().getBoolean("check.tamed")){
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Tameable){
            return (((Tameable) original).isTamed() || ((Tameable) nearby).isTamed());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Tameable){
            ((Tameable) spawned).setTamed(((Tameable) original).isTamed());
            ((Tameable) spawned).setOwner(((Tameable) original).getOwner());
        }
    }
}
