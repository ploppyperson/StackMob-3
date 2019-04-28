package uk.antiperson.stackmob.checks.trait.v1_14;

import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class CatTrait implements ApplicableTrait {

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

    @Override
    public String getConfigPath() {
        return "compare.cat-type";
    }
}
