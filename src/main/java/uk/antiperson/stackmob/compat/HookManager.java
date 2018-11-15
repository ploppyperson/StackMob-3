package uk.antiperson.stackmob.compat;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.hooks.*;

import java.util.HashMap;

// TODO: Custom hooks from other plugins.
public class HookManager {

    private StackMob sm;
    private HashMap<PluginCompat, PluginHook> hooks = new HashMap<>();
    public HookManager(StackMob sm){
        this.sm = sm;
    }

    public void onServerLoad(){
        new WorldGuardHook(this, sm).onLoad();
    }

    public void registerHooks(){
        new ProtocolLibHook(this, sm);
        new McmmoHook(this, sm);
        new CitizensHook(this, sm);
        new MiniaturePetsHook(this, sm);
        new MythicMobsHook(this, sm);
        new JobsHook(this, sm);
    }

    public void registerHook(PluginCompat hookEnum, PluginHook hook){
        hooks.put(hookEnum, hook);
        sm.getLogger().info("Plugin hook registered for " + hookEnum.getName() + " (" + hook.getPlugin().getDescription().getVersion() + ")");
    }

    public boolean isHookRegistered(PluginCompat hookEnum){
        return hooks.containsKey(hookEnum);
    }

    public PluginHook getHook(PluginCompat compat){
        return hooks.get(compat);
    }

    public boolean onEntityComparison(Entity entity, Entity nearby){
        for(PluginHook hook : hooks.values()){
            if(hook instanceof Comparable){
                Comparable comparable = (Comparable) hook;
                if(comparable.onEntityComparison(entity, nearby)){
                    return true;
                }
            }
        }
        return false;
    }

    public void onEntityClone(Entity entity){
        for(PluginHook hook : hooks.values()){
            if(hook instanceof CloneTrait){
                CloneTrait cloneTrait = (CloneTrait) hook;
                cloneTrait.setTrait(entity);
            }
        }
    }
}
