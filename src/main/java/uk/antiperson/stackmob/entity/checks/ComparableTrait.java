package uk.antiperson.stackmob.entity.checks;

import org.bukkit.entity.Entity;

public interface ComparableTrait {

    boolean checkTrait(Entity original, Entity nearby);

}
