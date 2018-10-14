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
    private MythicSupport mythicSupport;
    private WorldGuardSupport worldGuardSupport;
    private ProtocolSupport protocolSupport;
    public PluginSupport(StackMob sm){
        this.sm = sm;
    }

    public void setupWorldGuard(){
        Plugin pl = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if(pl != null && isWorldGuardCorrectVersion()){
            worldGuardSupport = new WorldGuardSupport(sm);
        }
    }

    public void startup(){
        Plugin pl = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        if(pl != null){
            protocolSupport = new ProtocolSupport(sm);
        }
        Plugin pl2 = Bukkit.getPluginManager().getPlugin("MythicMobs");
        if(sm.config.getCustomConfig().getBoolean("mythicmobs.enabled")){
            if(pl2 != null){
                mythicSupport = new MythicSupport(sm);
                if(!(isMiniPetCorrectVersion())){
                    sm.getLogger().warning("A version of MiniaturePets has been detected that is not supported!");
                    sm.getLogger().warning("MiniaturePets related mob checks will not work!");
                }
            }
        }
    }

    public void setMcmmoMetadata(Entity entity){
        Plugin mcmmo = sm.getServer().getPluginManager().getPlugin("mcMMO");
        if(sm.config.getCustomConfig().getBoolean("mcmmo.no-experience.enabled") && mcmmo != null){
            if(!sm.config.getCustomConfig().getStringList("mcmmo.no-experience.blacklist")
                    .contains(entity.getType().toString()) && mcmmo.isEnabled()){
                entity.setMetadata(GlobalValues.MCMMO_META, new FixedMetadataValue(mcmmo,false));
            }
        }
    }

    public boolean isMiniPet(Entity entity){
        Plugin miniPet = sm.getServer().getPluginManager().getPlugin("MiniaturePets");
        if(sm.config.getCustomConfig().getBoolean("check.is-miniature-pet") && miniPet != null){
            if(miniPet.isEnabled() && isMiniPetCorrectVersion()){
                return APIUtils.isEntityMob(entity);
            }
        }
        return false;
    }

    public boolean isNPC(Entity entity){
        Plugin citizens = sm.getServer().getPluginManager().getPlugin("Citizens");
        if(sm.config.getCustomConfig().getBoolean("check.is-citizens-npc")){
            if(citizens != null && citizens.isEnabled()){
                return entity.hasMetadata("NPC");
            }
        }
        return false;
    }

    public MythicSupport getMythicSupport(){
        return mythicSupport;
    }

    public ProtocolSupport getProtocolSupport(){
        return protocolSupport;
    }

    public WorldGuardSupport getWorldGuard(){
        return worldGuardSupport;
    }

    public boolean isProtocolSupportEnabled(){
        return getProtocolSupport() != null && sm.getVersionId() > 1;
    }

    public boolean isWorldGuardEnabled(){
        return getWorldGuard() != null;
    }

    public boolean isWorldGuardCorrectVersion(){
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            return true;
        }catch (ClassNotFoundException e){
            return false;
        }
    }

    public boolean isMiniPetCorrectVersion(){
        try {
            Class.forName("com.kirelcodes.miniaturepets.api.APIUtils");
            return true;
        }catch (ClassNotFoundException e){
            return false;
        }
    }
}
