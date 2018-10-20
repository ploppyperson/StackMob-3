package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitManager;

public class SheepShearedTrait implements ApplicableTrait {

    public SheepShearedTrait(TraitManager tc){
        if (tc.getStackMob().config.getCustomConfig().getBoolean("compare.sheep-wool-sheared")) {
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Sheep){
            return (((Sheep) original).isSheared() != ((Sheep) nearby).isSheared());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Sheep){
            ((Sheep) spawned).setSheared(((Sheep) original).isSheared());
        }
    }
}
