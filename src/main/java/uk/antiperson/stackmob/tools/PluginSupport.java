package uk.antiperson.stackmob.tools;

import com.kirelcodes.miniaturepets.api.APIUtils;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;
import uk.antiperson.stackmob.tools.extras.MythicSupport;

/**
 * Created by nathat on 05/08/17.
 */
public class PluginSupport {

    private StackMob sm;
    public PluginSupport(StackMob sm){
        this.sm = sm;
    }

    public void setMcmmoMetadata(Entity entity){
        if(sm.config.getCustomConfig().getBoolean("mcmmo.no-experience.enabled") && sm.getServer().getPluginManager().getPlugin("mcMMO") != null){
            if(!sm.config.getCustomConfig().getStringList("mcmmo.no-experience.blacklist")
                    .contains(entity.getType().toString()) && sm.getServer().getPluginManager().isPluginEnabled("mcMMO")){
                entity.setMetadata(GlobalValues.MCMMO_META, new FixedMetadataValue(sm.getServer().getPluginManager().getPlugin("mcMMO"), false));
            }
        }
    }

    public boolean isMiniPet(Entity entity){
        if(sm.config.getCustomConfig().getBoolean("check.is-miniature-pet") && sm.getServer().getPluginManager().getPlugin("MiniaturePets") != null){
            if(sm.getServer().getPluginManager().isPluginEnabled("MiniaturePets")){
                return APIUtils.isEntityMob(entity);
            }
        }
        return false;
    }

    public MythicSupport getMythicSupport(){
        if(sm.config.getCustomConfig().getBoolean("mythicmobs.enabled") && sm.getServer().getPluginManager().getPlugin("MythicMobs") != null){
            if(sm.getServer().getPluginManager().isPluginEnabled("MythicMobs")){
                return new MythicSupport(sm);
            }
        }
        return null;
    }


}
