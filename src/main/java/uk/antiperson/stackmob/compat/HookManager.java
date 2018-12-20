package uk.antiperson.stackmob.compat;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.hooks.*;

import java.util.EnumMap;
import java.util.Map;

public class HookManager {

    private StackMob sm;
    private Map<PluginCompat, PluginHook> hooks = new EnumMap<>(PluginCompat.class);
    public HookManager(StackMob sm){
        this.sm = sm;
    }

    public void onServerLoad(){
        new WorldGuardHook(this, sm).onLoad();
    }

    public void registerHooks(){
        enableHook(new WorldGuardHook(this, sm));
        enableHook(new ProtocolLibHook(this, sm));
        enableHook(new McmmoHook(this, sm));
        enableHook(new CitizensHook(this, sm));
        enableHook(new MiniaturePetsHook(this, sm));
        enableHook(new MythicMobsHook(this, sm));
        enableHook(new JobsHook(this, sm));
        enableHook(new CustomDropsHook(this, sm));
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

    private void enableHook(PluginHook hook){
        if(hook.getPlugin() == null){
            return;
        }
        hook.enable();
        if(!isHookRegistered(hook.getPluginCompat())){
            if(hook instanceof Errorable){
                ((Errorable) hook).disable();
            }
        }
    }
}
