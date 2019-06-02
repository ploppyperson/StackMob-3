package uk.antiperson.stackmob.bcompat.v1_14.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.MushroomCow;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class MooshroomTrait implements ApplicableTrait {

    public String getConfigPath() {
        return "compare.mooshroom-variant";
    }

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
}
