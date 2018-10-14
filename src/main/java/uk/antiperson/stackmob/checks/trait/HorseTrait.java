package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitChecks;

public class HorseTrait implements ApplicableTrait {

    public HorseTrait(TraitChecks tc){
        if(tc.getStackMob().config.getCustomConfig().getBoolean("compare.horse-color")) {
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Horse){
            return (((Horse) original).getColor() != ((Horse) nearby).getColor());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Horse){
            ((Horse) spawned).setColor(((Horse) original).getColor());
            // another trait?
        }
    }
}
