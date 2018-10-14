package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitChecks;

public class LlamaTrait implements ApplicableTrait {

    public LlamaTrait(TraitChecks tc){
        if (tc.getStackMob().config.getCustomConfig().getBoolean("compare.llama-color")){
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Llama){
            return (((Llama) original).getColor() != ((Llama) nearby).getColor());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Llama){
            ((Llama) spawned).setColor(((Llama) original).getColor());
        }
    }
}
