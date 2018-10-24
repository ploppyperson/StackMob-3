package uk.antiperson.stackmob.compat;

import org.bukkit.entity.Entity;

public interface Comparable extends PluginChecks{

    boolean onEntityComparison(Entity entity, Entity nearby);
}
