package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class ParrotTrait implements ApplicableTrait {

    public String getConfigPath(){
        return "compare.parrot-color";
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
