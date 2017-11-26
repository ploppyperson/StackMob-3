package uk.antiperson.stackmob.services;

import com.kirelcodes.miniaturepets.api.APIUtils;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.Config;
import uk.antiperson.stackmob.config.ConfigLoader;

@AllArgsConstructor
public class SupportService {

    private Config config;
    private PluginManager pluginManager;

    private boolean isEnabled(String name) {
        return pluginManager.isPluginEnabled(name);
    }

    public void setMcMMOMetadata(Entity entity) {
        if (!isEnabled("mcMMO")) {
            return;
        }
        if (config.get().getBoolean("mcmmo.no-experience.enabled")
                && !config.get().getStringList("mcmmo.no-experience.blacklist").contains(entity.getType().toString())) {
            entity.setMetadata(GlobalValues.MCMMO_META, new FixedMetadataValue(pluginManager.getPlugin("mcMMO"), false));
        }
    }

    public boolean isMiniPet(Entity entity) {
        return isEnabled("MiniaturePets")
                && config.get().getBoolean("check.is-miniature-pet")
                && APIUtils.isEntityMob(entity);
    }

    public boolean isMythicMob(Entity entity) {
        if (!isEnabled("MythicMobs") || !config.get().getBoolean("mythicmobs.enabled")) {
            return false;
        }
        MobManager mobManager = MythicMobs.inst().getMobManager();
        return mobManager.isActiveMob(entity.getUniqueId());
    }

    public String getMythicName(Entity entity) {
        if (!isEnabled("MythicMobs") || !config.get().getBoolean("mythicmobs.enabled")) {
            return null;
        }
        MobManager mobManager = MythicMobs.inst().getMobManager();
        ActiveMob mobInstance = mobManager.getMythicMobInstance(entity);
        return mobInstance.getType().getInternalName();
    }

    public Entity spawnMythicMob(Entity entity) {
        if (!isEnabled("MythicMobs") || !config.get().getBoolean("mythicmobs.enabled")) {
            return null;
        }
        MobManager mobManager = MythicMobs.inst().getMobManager();
        ActiveMob mobInstance = mobManager.getMythicMobInstance(entity);
        return mobManager.spawnMob(mobInstance.getType().getInternalName(), entity.getLocation()).getLivingEntity();
    }

    public boolean notMatchMythicMobs(Entity entity, Entity nearby) {
        if (!isEnabled("MythicMobs") || !config.get().getBoolean("mythicmobs.enabled")) {
            return false;
        }
        MobManager mobManager = MythicMobs.inst().getMobManager();
        if (!mobManager.isActiveMob(nearby.getUniqueId()) || !mobManager.isActiveMob(entity.getUniqueId())) {
            return false;
        }
        ActiveMob entityMob = mobManager.getMythicMobInstance(entity);
        ActiveMob nearbyMob = mobManager.getMythicMobInstance(nearby);
        return config.get().getStringList("mythicmobs.blacklist").contains(nearbyMob.getType().getInternalName())
                || config.get().getStringList("mythicmobs.blacklist").contains(entityMob.getType().getInternalName())
                || !nearbyMob.getType().getInternalName().equals(entityMob.getType().getInternalName());
    }

}
