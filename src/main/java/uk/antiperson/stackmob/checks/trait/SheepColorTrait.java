package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitChecks;

public class SheepColorTrait implements ApplicableTrait {

    public SheepColorTrait(TraitChecks tc){
        if (tc.getStackMob().config.getCustomConfig().getBoolean("compare.sheep-wool-color")) {
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Sheep){
            return (((Sheep) original).getColor() != ((Sheep) nearby).getColor());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Sheep){
            ((Sheep) spawned).setColor(((Sheep) original).getColor());
        }
    }
}
