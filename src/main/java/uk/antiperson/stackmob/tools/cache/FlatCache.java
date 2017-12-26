package uk.antiperson.stackmob.tools.cache;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.config.ConfigLoader;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by nathat on 30/07/17.
 */
public class FlatCache extends ConfigLoader implements Cache {

    public HashMap<UUID, Integer> amountCache = new HashMap<>();

    public FlatCache(StackMob sm){
        super(sm, "cache");
        fc = YamlConfiguration.loadConfiguration(f);
    }

    public void load(){
        for(String key : fc.getKeys(false)){
            amountCache.put(UUID.fromString(key), fc.getInt(key));
        }
        f.delete();
    }

    public void close(){
        fc.options().header("This file should not be modified.");
        for(UUID key : amountCache.keySet()){
            fc.set(key.toString(), amountCache.get(key));
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
                    fc.set(currentEntity.getUniqueId().toString(), -69);
                }
            }
        }
        try {
            fc.save(f);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void write(UUID uuid, int value){
        amountCache.put(uuid, value);
    }

    public int read(UUID uuid){
        return amountCache.get(uuid);
    }

    public boolean contains(UUID uuid){
        return amountCache.containsKey(uuid);
    }

    public void remove(UUID uuid){
        amountCache.remove(uuid);
    }

    public Set<UUID> getKeys(){
        return amountCache.keySet();
    }
}
