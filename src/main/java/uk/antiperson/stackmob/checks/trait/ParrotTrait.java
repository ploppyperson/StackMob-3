package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitChecks;

public class ParrotTrait implements ApplicableTrait {

    public ParrotTrait(TraitChecks tc){
        if(tc.getStackMob().config.getCustomConfig().getBoolean("compare.parrot-color")){
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Parrot){
            return ((Parrot) original).getVariant() != ((Parrot) nearby).getVariant();
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Parrot){
            ((Parrot) spawned).setVariant(((Parrot) original).getVariant());
        }
    }
}
