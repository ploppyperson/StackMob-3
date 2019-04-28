package uk.antiperson.stackmob.checks.trait.common;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class LoveTrait implements ApplicableTrait {

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if (original instanceof Animals) {
            return ((Animals) original).isLoveMode() || ((Animals) nearby).isLoveMode();
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if (original instanceof Animals) {
            ((Animals) spawned).setLoveModeTicks(((Animals) original).getLoveModeTicks());
        }
    }

    @Override
    public String getConfigPath() {
        return "compare.love-mode";
    }
}
