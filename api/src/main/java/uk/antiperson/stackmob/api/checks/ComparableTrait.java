package uk.antiperson.stackmob.api.checks;

import org.bukkit.entity.Entity;

public interface ComparableTrait {

    boolean checkTrait(Entity original, Entity nearby);

    String getConfigPath();
}
