package uk.antiperson.stackmob.api.compat;

import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.api.StackMob;

public interface PluginHook extends PluginChecks {
    String getPluginName();

    Plugin getPlugin();

    StackMob getStackMob();

    HookManager getHookManager();

    PluginCompat getPluginCompat();
}
