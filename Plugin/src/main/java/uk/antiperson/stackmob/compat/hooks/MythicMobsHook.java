package uk.antiperson.stackmob.compat.hooks;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.HookManager;
import uk.antiperson.stackmob.compat.PluginHook;
import uk.antiperson.stackmob.api.compat.PluginCompat;
import uk.antiperson.stackmob.api.compat.Comparable;

import java.util.List;

public class MythicMobsHook extends PluginHook implements Comparable {

    public MythicMobsHook(HookManager hm, StackMob sm){
        super(hm, sm, PluginCompat.MYTHICMOBS);
    }

    @Override
    public void enable(){
        if(getStackMob().getCustomConfig().getBoolean("mythicmobs.enabled")){
            getHookManager().registerHook(getPluginCompat(), this);
        }
    }

    @Override
    public boolean onEntityComparison(Entity original, Entity nearby){
        if(isMythicMob(original) && isMythicMob(nearby)){
            if(isAllBlacklisted()){
                return true;
            }
            ActiveMob activeMobO = getMobManager().getMythicMobInstance(original);
            ActiveMob activeMobN = getMobManager().getMythicMobInstance(nearby);
            if(!(activeMobO.getType().equals(activeMobN.getType()))){
                return true;
            }
            return isInBlacklist(activeMobO);
        }
        return (isMythicMob(original) || isMythicMob(nearby));
    }


    private MobManager getMobManager(){
        return ((MythicMobs) getPlugin()).getMobManager();
    }

    private List<String> getBlacklist(){
        return getStackMob().getCustomConfig().getStringList("mythicmobs.blacklist");
    }

    public String getDisplayName(Entity entity){
        return "" + getMobManager().getMythicMobInstance(entity).getType().getDisplayName();
    }

    public boolean isMythicMob(Entity entity){
        return getMobManager().isActiveMob(entity.getUniqueId());
    }

    private boolean isInBlacklist(ActiveMob entity){
        String name = entity.getType().getInternalName();
        return getBlacklist().contains(name);
    }

    private boolean isAllBlacklisted(){
        return getBlacklist().contains("ALL");
    }

    public Entity spawnMythicMob(Location spawnLocation, Entity original){
        ActiveMob activeMob = getMobManager().getMythicMobInstance(original);
        ActiveMob clone = getMobManager().spawnMob(activeMob.getType().getInternalName(), spawnLocation);
        if(clone != null){
            return clone.getLivingEntity();
        }
        return null;
    }
}
