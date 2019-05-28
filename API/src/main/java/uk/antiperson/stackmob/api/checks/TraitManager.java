package uk.antiperson.stackmob.api.checks;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.StackMob;

public interface TraitManager {
    void registerTraits();

    boolean checkTraits(Entity original, Entity nearby);

    boolean checkTraits(Entity original);

    void applyTraits(Entity orginal, Entity spawned);

    void registerTrait(ComparableTrait trait);

    StackMob getStackMob();
}
