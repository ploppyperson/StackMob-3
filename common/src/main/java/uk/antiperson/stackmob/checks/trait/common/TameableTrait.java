package uk.antiperson.stackmob.checks.trait.common;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;
import uk.antiperson.stackmob.api.checks.SingleTrait;

public class TameableTrait implements ApplicableTrait, SingleTrait {

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Tameable){
            return (((Tameable) original).isTamed() || ((Tameable) nearby).isTamed());
        }
        return false;
    }

    @Override
    public boolean checkTrait(Entity original) {
        if(original instanceof Tameable){
            return ((Tameable) original).isTamed();
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

    @Override
    public String getConfigPath() {
        return "check.tamed";
    }
}
