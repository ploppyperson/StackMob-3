package uk.antiperson.stackmob.api.compat;

import org.bukkit.entity.Entity;

public interface HookManager {
    void onServerLoad();

    void registerHooks();

    void registerHook(PluginCompat hookEnum, PluginHook hook);

    boolean isHookRegistered(PluginCompat hookEnum);

    PluginHook getHook(PluginCompat compat);

    boolean onEntityComparison(Entity entity, Entity nearby);

    boolean cantStack(Entity entity);

    void onEntityClone(Entity entity);
}
