package uk.antiperson.stackmob.tools.config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by nathat on 30/07/17.
 */
public class Cache {

    public HashMap<UUID, Integer> amountCache = new HashMap<>();

    private StackMob sm;
    private File cacheFile;
    private FileConfiguration cacheCon;
    public Cache(StackMob sm){
        this.sm = sm;
        cacheFile = new File(sm.getDataFolder(), "cache.yml");
        cacheCon = YamlConfiguration.loadConfiguration(cacheFile);
    }

    public void loadCache(){
        for(String key : cacheCon.getKeys(false)){
            amountCache.put(UUID.fromString(key), cacheCon.getInt(key));
        }
        cacheFile.delete();
    }

    public void saveCache(){
        cacheCon.options().header("This file should not be modified.");
        for(UUID key : amountCache.keySet()){
            cacheCon.set(key.toString(), amountCache.get(key));
        }
        for (World world : Bukkit.getWorlds()){
            for(Entity currentEntity : world.getLivingEntities()){
                if(currentEntity.hasMetadata(GlobalValues.METATAG) &&
                        currentEntity.getMetadata(GlobalValues.METATAG).size() > 0 &&
                        currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1){
                    cacheCon.set(currentEntity.getUniqueId().toString(), currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt());
                }
                if(currentEntity.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR) &&
                        currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).size() > 0 &&
                        currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()){
                    cacheCon.set(currentEntity.getUniqueId().toString(), -69);
                }
            }
        }
        try {
            cacheCon.save(cacheFile);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
