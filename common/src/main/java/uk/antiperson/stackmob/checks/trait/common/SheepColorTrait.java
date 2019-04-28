package uk.antiperson.stackmob.checks.trait.common;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class SheepColorTrait implements ApplicableTrait {

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

    @Override
    public String getConfigPath() {
        return "compare.sheep-wool-color";
    }
}
