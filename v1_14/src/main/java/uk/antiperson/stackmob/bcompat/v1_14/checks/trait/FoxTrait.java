package uk.antiperson.stackmob.bcompat.v1_14.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class FoxTrait implements ApplicableTrait {

    public String getConfigPath() {
        return "compare.fox-type";
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if (original instanceof Fox) {
            return ((Fox) original).getFoxType() != ((Fox) nearby).getFoxType();
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if (original instanceof Fox) {
            ((Fox) spawned).setFoxType(((Fox) original).getFoxType());
        }
    }
}
