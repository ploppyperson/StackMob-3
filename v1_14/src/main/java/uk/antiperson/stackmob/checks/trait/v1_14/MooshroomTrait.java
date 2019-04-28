package uk.antiperson.stackmob.checks.trait.v1_14;

import org.bukkit.entity.Entity;
import org.bukkit.entity.MushroomCow;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class MooshroomTrait implements ApplicableTrait {

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if (original instanceof MushroomCow) {
            return ((MushroomCow) original).getVariant() != ((MushroomCow) nearby).getVariant();
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if (original instanceof MushroomCow) {
            ((MushroomCow) spawned).setVariant(((MushroomCow) original).getVariant());
        }
    }

    @Override
    public String getConfigPath() {
        return "compare.mooshroom-color";
    }
}
