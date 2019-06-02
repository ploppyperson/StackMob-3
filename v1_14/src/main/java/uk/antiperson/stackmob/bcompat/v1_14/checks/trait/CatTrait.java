package uk.antiperson.stackmob.bcompat.v1_14.checks.trait;

import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class CatTrait implements ApplicableTrait {

    public String getConfigPath() {
        return "compare.cat-type";
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if (original instanceof Cat) {
            return ((Cat) original).getCatType() != ((Cat) nearby).getCatType();
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if (original instanceof Cat) {
            ((Cat) spawned).setCatType(((Cat) original).getCatType());
        }
    }
}
