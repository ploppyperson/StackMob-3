package uk.antiperson.stackmob.api.checks;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.IStackMob;

public interface ITraitManager {
    void registerTraits();

    boolean checkTraits(Entity original, Entity nearby);

    boolean checkTraits(Entity original);

    void applyTraits(Entity orginal, Entity spawned);

    void registerTrait(ComparableTrait trait);

    IStackMob getStackMob();
}
