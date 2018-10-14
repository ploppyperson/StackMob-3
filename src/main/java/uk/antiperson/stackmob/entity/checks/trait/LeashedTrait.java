package uk.antiperson.stackmob.entity.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.entity.checks.ComparableTrait;
import uk.antiperson.stackmob.entity.checks.TraitChecks;

public class LeashedTrait implements ComparableTrait {

    public LeashedTrait(TraitChecks tc){
        if (tc.getStackMob().config.getCustomConfig().getBoolean("check.leashed")) {
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
