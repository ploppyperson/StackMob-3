package uk.antiperson.stackmob.compat;

import org.bukkit.entity.Entity;

public interface Testable extends PluginChecks {

    boolean cantStack(Entity entity);
}
