package uk.antiperson.stackmob.compat.hooks;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.Comparable;
import uk.antiperson.stackmob.compat.HookManager;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.PluginHook;

public class WorldGuardHook extends PluginHook implements Comparable {

    private WorldGuardCompat worldGuardCompat;
    public WorldGuardHook(HookManager hm, StackMob sm){
        super(hm, sm, PluginCompat.WORLDGUARD);
    }

    public void onLoad(){
        if(getPlugin() != null) {
            if(isCorrectVersion()){
                new WorldGuardCompat(getStackMob()).registerFlag();
            }
        }
    }

    @Override
    public void enable(){
        if(getStackMob().getCustomConfig().getBoolean("worldguard-support")){
            if(isCorrectVersion()) {
                getHookManager().registerHook(PluginCompat.WORLDGUARD, this);
            }else{
                getStackMob().getLogger().warning("In order for this functionality to work, WorldGuard 7.0 or later needs to be installed.");
            }
        }
    }

    @Override
    public boolean onEntityComparison(Entity entity, Entity nearby){
        return worldGuardCompat.test(entity) || worldGuardCompat.test(nearby);
    }

    private boolean isCorrectVersion(){
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            return true;
        }catch (ClassNotFoundException e){
            return false;
        }
    }
}
