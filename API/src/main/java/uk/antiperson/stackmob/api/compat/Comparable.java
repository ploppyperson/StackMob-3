package uk.antiperson.stackmob.api.compat;

import org.bukkit.entity.Entity;

public interface Comparable {

    boolean onEntityComparison(Entity entity, Entity nearby);
}
