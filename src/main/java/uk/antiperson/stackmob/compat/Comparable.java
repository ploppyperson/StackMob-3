package uk.antiperson.stackmob.compat;

import org.bukkit.entity.Entity;

public interface Comparable {

    boolean onEntityComparison(Entity entity, Entity nearby);
}
