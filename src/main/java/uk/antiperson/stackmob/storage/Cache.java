package uk.antiperson.stackmob.storage;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.StackMob;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Cache {

    private File cacheFile;
    private FileConfiguration cacheConfig;

    private Map<UUID, Integer> amountCache;

    public Cache(StackMob plugin) {
        cacheFile = new File(plugin.getDataFolder(), "cache.yml");
        cacheConfig = YamlConfiguration.loadConfiguration(cacheFile);
        amountCache = new LinkedHashMap<>();
    }

    public boolean isCached(UUID entityUuid) {
        return amountCache.containsKey(entityUuid);
    }

    public int getAmount(UUID entityUuid) {
        return amountCache.get(entityUuid);
    }

    public void put(UUID entityUuid, int amount) {
        amountCache.put(entityUuid, amount);
    }

    public void remove(UUID entityUuid) {
        amountCache.remove(entityUuid);
    }

    public int getEntitiesCount() {
        return amountCache.size();
    }

    public int getTotalEntitiesCount() {
        return amountCache.values().stream().mapToInt(Number::intValue).filter(amount -> amount > 0).sum();
    }

    public void loadCache() {
        for (String key : cacheConfig.getKeys(false)) {
            amountCache.put(UUID.fromString(key), cacheConfig.getInt(key));
        }
        cacheFile.delete();
    }

    public void saveCache() {
        cacheConfig.options().header("This file should not be modified.");
        for (UUID key : amountCache.keySet()) {
            cacheConfig.set(key.toString(), amountCache.get(key));
        }
        for (World world : Bukkit.getWorlds()) {
            for (Entity currentEntity : world.getLivingEntities()) {
                if (currentEntity.hasMetadata(GlobalValues.METATAG) &&
                        currentEntity.getMetadata(GlobalValues.METATAG).size() > 0 &&
                        currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1) {
                    cacheConfig.set(currentEntity.getUniqueId().toString(), currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt());
                }
                if (currentEntity.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR) &&
                        currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).size() > 0 &&
                        currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()) {
                    cacheConfig.set(currentEntity.getUniqueId().toString(), -69);
                }
            }
        }
        try {
            cacheConfig.save(cacheFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
