package uk.antiperson.stackmob.compat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.StackMobPlugin;
import uk.antiperson.stackmob.api.compat.IHookManager;
import uk.antiperson.stackmob.api.compat.PluginCompat;

public abstract class PluginHook implements uk.antiperson.stackmob.api.compat.PluginHook {

    private StackMobPlugin stackMob;
    private IHookManager hookManager;
    private Plugin plugin;
    private String pluginName;
    private PluginCompat pluginCompat;
    public PluginHook(IHookManager hm, StackMobPlugin sm, PluginCompat hooks){
        plugin = Bukkit.getPluginManager().getPlugin(hooks.getName());
        pluginName = hooks.getName();
        stackMob = sm;
        pluginCompat = hooks;
        hookManager = hm;
    }

    @Override
    public String getPluginName() {
        return pluginName;
    }

    @Override
    public Plugin getPlugin(){
        return plugin;
    }

    @Override
    public StackMobPlugin getStackMob() {
        return stackMob;
    }

    @Override
    public IHookManager getHookManager() {
        return hookManager;
    }

    @Override
    public PluginCompat getPluginCompat() {
        return pluginCompat;
    }

}
