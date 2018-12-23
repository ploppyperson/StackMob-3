package uk.antiperson.stackmob.compat.hooks;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.HookManager;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.PluginHook;
import uk.antiperson.stackmob.compat.Testable;

public class WorldGuardHook extends PluginHook implements Testable {

    private WorldGuardCompat worldGuardCompat;
    public WorldGuardHook(HookManager hm, StackMob sm){
        super(hm, sm, PluginCompat.WORLDGUARD);
        if(isCorrectVersion()){
            worldGuardCompat = new WorldGuardCompat(sm);
        }
    }

    public void onLoad(){
        if(getPlugin() != null) {
            if(worldGuardCompat != null){
                worldGuardCompat.registerFlag();
            }
        }
    }

    @Override
    public void enable(){
        if(getStackMob().getCustomConfig().getBoolean("worldguard-support")){
            if(worldGuardCompat != null) {
                getHookManager().registerHook(PluginCompat.WORLDGUARD, this);
            }else{
                getStackMob().getLogger().warning("In order for this functionality to work, WorldGuard 7.0 or later needs to be installed.");
            }
        }
    }

    @Override
    public boolean cantStack(Entity entity) {
        return worldGuardCompat.test(entity);
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
