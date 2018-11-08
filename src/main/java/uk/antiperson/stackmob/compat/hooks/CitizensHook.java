package uk.antiperson.stackmob.compat.hooks;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.Comparable;
import uk.antiperson.stackmob.compat.HookManager;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.PluginHook;

public class CitizensHook extends PluginHook implements Comparable {

    public CitizensHook(HookManager hm, StackMob sm){
        super(hm, sm, PluginCompat.CITIZENS);
    }

    @Override
    public void enable(){
        if(getStackMob().getCustomConfig().getBoolean("check.is-citizens-npc")){
            getHookManager().registerHook(getPluginCompat(), this);
        }
    }

    @Override
    public boolean onEntityComparison(Entity entity, Entity nearby){
        return checkMetadata(entity) || checkMetadata(nearby);
    }

    private boolean checkMetadata(Entity entity){
        return entity.hasMetadata("NPC");
    }

}
