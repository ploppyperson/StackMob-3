package uk.antiperson.stackmob.checks;

import org.bukkit.entity.Entity;

public interface ApplicableTrait extends ComparableTrait{

    void applyTrait(Entity original, Entity spawned);
}
