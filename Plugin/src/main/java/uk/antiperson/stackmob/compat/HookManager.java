package uk.antiperson.stackmob.compat;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.compat.*;
import uk.antiperson.stackmob.api.compat.Comparable;
import uk.antiperson.stackmob.compat.hooks.*;

import java.util.EnumMap;
import java.util.Map;

public class HookManager implements IHookManager {

    private StackMob sm;
    private Map<PluginCompat, uk.antiperson.stackmob.api.compat.PluginHook> hooks = new EnumMap<>(PluginCompat.class);
    public HookManager(StackMob sm){
        this.sm = sm;
    }

    @Override
    public void onServerLoad(){
        new WorldGuardHook(this, sm).onLoad();
    }

    @Override
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

    @Override
    public void registerHook(PluginCompat hookEnum, uk.antiperson.stackmob.api.compat.PluginHook hook){
        hooks.put(hookEnum, hook);
        sm.getLogger().info("Plugin hook registered for " + hookEnum.getName() + " (" + hook.getPlugin().getDescription().getVersion() + ")");
    }

    @Override
    public boolean isHookRegistered(PluginCompat hookEnum){
        return hooks.containsKey(hookEnum);
    }

    @Override
    public uk.antiperson.stackmob.api.compat.PluginHook getHook(PluginCompat compat){
        return hooks.get(compat);
    }

    @Override
    public boolean onEntityComparison(Entity entity, Entity nearby){
        for(uk.antiperson.stackmob.api.compat.PluginHook hook : hooks.values()){
            if(hook instanceof uk.antiperson.stackmob.api.compat.Comparable){
                uk.antiperson.stackmob.api.compat.Comparable comparable = (Comparable) hook;
                if(comparable.onEntityComparison(entity, nearby)){
                    return true;
                }
            }else if(hook instanceof Testable){
                if(cantStack(hook, entity) || cantStack(hook, nearby)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean cantStack(Entity entity){
        for(uk.antiperson.stackmob.api.compat.PluginHook hook : hooks.values()){
            if(hook instanceof Testable){
                if(cantStack(hook, entity)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean cantStack(uk.antiperson.stackmob.api.compat.PluginHook hook, Entity entity){
        Testable testable = (Testable) hook;
        return testable.cantStack(entity);
    }

    @Override
    public void onEntityClone(Entity entity){
        for(uk.antiperson.stackmob.api.compat.PluginHook hook : hooks.values()){
            if(hook instanceof CloneTrait){
                CloneTrait cloneTrait = (CloneTrait) hook;
                cloneTrait.setTrait(entity);
            }
        }
    }

    private void enableHook(uk.antiperson.stackmob.api.compat.PluginHook hook){
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
