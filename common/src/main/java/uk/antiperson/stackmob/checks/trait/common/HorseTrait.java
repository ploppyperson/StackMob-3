package uk.antiperson.stackmob.checks.trait.common;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class HorseTrait implements ApplicableTrait {

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

    @Override
    public String getConfigPath() {
        return "compare.horse-color";
    }
}
