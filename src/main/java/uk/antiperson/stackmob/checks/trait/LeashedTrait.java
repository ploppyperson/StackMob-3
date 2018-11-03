package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.checks.ComparableTrait;
import uk.antiperson.stackmob.checks.TraitManager;

public class LeashedTrait implements ComparableTrait {

    public LeashedTrait(TraitManager tc){
        if (tc.getStackMob().getCustomConfig().getBoolean("check.leashed")) {
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof LivingEntity){
            return (((LivingEntity) original).isLeashed() || ((LivingEntity) nearby).isLeashed());
        }
        return false;
    }
}
