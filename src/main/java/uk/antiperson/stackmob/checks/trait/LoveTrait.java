package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitManager;

public class LoveTrait implements ApplicableTrait {

    public LoveTrait(TraitManager tm){
        if (tm.getStackMob().getCustomConfig().getBoolean("compare.love-mode")) {
            tm.registerTrait(this);
        }
    }

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
}
