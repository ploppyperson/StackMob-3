package uk.antiperson.stackmob.entity.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import uk.antiperson.stackmob.entity.checks.ApplicableTrait;
import uk.antiperson.stackmob.entity.checks.TraitChecks;

public class ZombieTrait implements ApplicableTrait {

    public ZombieTrait(TraitChecks tc){
        if(tc.getStackMob().config.getCustomConfig().getBoolean("compare.entity-age")) {
            tc.registerTrait(this);
        }
    }

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
}
