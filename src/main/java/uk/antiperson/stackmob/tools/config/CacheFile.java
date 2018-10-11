package uk.antiperson.stackmob.tools.config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nathat on 30/07/17.
 */
public class CacheFile extends ConfigLoader{

    public HashMap<UUID, Integer> amountCache = new HashMap<>();

    public CacheFile(StackMob sm){
        super(sm, "cache");
        fc = YamlConfiguration.loadConfiguration(f);
    }

    public void loadCache(){
        for(String key : fc.getKeys(false)){
            amountCache.put(UUID.fromString(key), fc.getInt(key));
        }
    }

    public void saveCache(){
        f.delete();
        fc = YamlConfiguration.loadConfiguration(f);
        fc.options().header("This file should not be modified.");
        for(Map.Entry<UUID, Integer> entry: amountCache.entrySet()){
            fc.set(entry.getKey().toString(), entry.getValue());
        }
        for (World world : Bukkit.getWorlds()){
            for(Entity currentEntity : world.getLivingEntities()){
                if(currentEntity.hasMetadata(GlobalValues.METATAG) &&
                        currentEntity.getMetadata(GlobalValues.METATAG).size() > 0 &&
                        currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1){
                    fc.set(currentEntity.getUniqueId().toString(), currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt());
                }
                if(currentEntity.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR) &&
                        currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).size() > 0 &&
                        currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()){
                    fc.set(currentEntity.getUniqueId().toString(), -1);
                }
            }
        }
        try {
            fc.save(f);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
