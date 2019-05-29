package uk.antiperson.stackmob.api.compat;

import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.api.IStackMob;

public interface PluginHook extends PluginChecks {
    String getPluginName();

    Plugin getPlugin();

    IStackMob getStackMob();

    IHookManager getHookManager();

    PluginCompat getPluginCompat();
}
