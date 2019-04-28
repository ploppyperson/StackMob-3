package uk.antiperson.stackmob.checks.trait.common;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class SlimeTrait implements ApplicableTrait {

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Slime){
            return  (((Slime) original).getSize() != ((Slime) nearby).getSize());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Slime){
            ((Slime) spawned).setSize(((Slime) original).getSize());
        }
    }

    @Override
    public String getConfigPath() {
        return "compare.slime-size";
    }
}
