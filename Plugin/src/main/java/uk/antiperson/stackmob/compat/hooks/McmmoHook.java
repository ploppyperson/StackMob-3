package uk.antiperson.stackmob.compat.hooks;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMobPlugin;
import uk.antiperson.stackmob.api.compat.HookManager;
import uk.antiperson.stackmob.api.compat.CloneTrait;
import uk.antiperson.stackmob.api.compat.PluginCompat;
import uk.antiperson.stackmob.compat.PluginHook;
import uk.antiperson.stackmob.api.tools.GlobalValues;

public class McmmoHook extends PluginHook implements CloneTrait {

    public McmmoHook(HookManager hm, StackMobPlugin sm){
        super(hm, sm, PluginCompat.MCMMO);
    }

    @Override
    public void enable(){
        if(getStackMob().getCustomConfig().getBoolean("mcmmo.no-experience.enabled")){
            getHookManager().registerHook(getPluginCompat(), this);
        }
    }

    @Override
    public void setTrait(Entity entity){
        if(!getStackMob().getCustomConfig().getStringList("mcmmo.no-experience.blacklist")
                .contains(entity.getType().toString())){
            entity.setMetadata(GlobalValues.MCMMO_META, new FixedMetadataValue(getPlugin(),false));
        }
    }

}
