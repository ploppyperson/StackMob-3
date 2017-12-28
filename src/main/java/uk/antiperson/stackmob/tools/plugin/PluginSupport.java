package uk.antiperson.stackmob.tools.plugin;

import com.kirelcodes.miniaturepets.api.APIUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

/**
 * This could do with being completely redone.
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

    public ProtocolSupport getProtocolSupport(){
        Plugin pl = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        if(pl != null && pl.isEnabled()){
            return new ProtocolSupport(sm);
        }
        return null;
    }

    public WorldGuardSupport getWorldGuard(){
        Plugin pl = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if(pl != null){
            return new WorldGuardSupport(sm);
        }
        return null;
    }

    public boolean isProtocolSupportEnabled(){
        return getProtocolSupport() != null;
    }

    public boolean isWorldGuardEnabled(){
        return getWorldGuardPlugin() != null && Integer.valueOf(getWorldGuardPlugin().getDescription().getVersion().replace(".", "").split(";")[0]) >= 620;
    }

    public Plugin getWorldGuardPlugin(){
        return Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
    }
}
