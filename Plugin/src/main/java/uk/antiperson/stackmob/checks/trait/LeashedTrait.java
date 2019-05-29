package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.api.checks.TraitManager;
import uk.antiperson.stackmob.api.checks.ComparableTrait;
import uk.antiperson.stackmob.api.checks.SingleTrait;

public class LeashedTrait implements ComparableTrait, SingleTrait {

    public String getConfigPath(){
        return "check.leashed";
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof LivingEntity){
            return (((LivingEntity) original).isLeashed() || ((LivingEntity) nearby).isLeashed());
        }
        return false;
    }

    @Override
    public boolean checkTrait(Entity original) {
        if(original instanceof LivingEntity){
            return ((LivingEntity) original).isLeashed();
        }
        return false;
    }
}
