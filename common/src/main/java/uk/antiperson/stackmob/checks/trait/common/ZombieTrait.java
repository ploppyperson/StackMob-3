package uk.antiperson.stackmob.checks.trait.common;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class ZombieTrait implements ApplicableTrait {

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Zombie){
            return (((Zombie) original).isBaby() != ((Zombie) nearby).isBaby());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Zombie) {
            ((Zombie) spawned).setBaby(((Zombie) original).isBaby());
        }
    }

    @Override
    public String getConfigPath() {
        return "compare.entity-age";
    }
}
