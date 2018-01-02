package uk.antiperson.stackmob.tools.cache;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.config.ConfigLoader;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Created by nathat on 30/07/17.
 */
public class FlatCache extends ConfigLoader implements Cache {

    private HashMap<UUID, Integer> amountCache = new HashMap<>();

    private StackMob sm;
    public FlatCache(StackMob sm){
        super(sm, "cache");
        this.sm = sm;
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void load(){
        fileConfiguration.getKeys(false).forEach(key -> amountCache.put(UUID.fromString(key), fileConfiguration.getInt(key)));
        file.delete();
    }

    public void close(){
        file.delete();
        fileConfiguration.options().header("This file should not be modified.");
        amountCache.keySet().forEach(key -> fileConfiguration.set(key.toString(), amountCache.get(key)));

        for (World world : Bukkit.getWorlds()){
            for(Entity currentEntity : world.getLivingEntities()){
                if(currentEntity.hasMetadata(GlobalValues.METATAG) &&
                        currentEntity.getMetadata(GlobalValues.METATAG).size() > 0 &&
                        currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt() > 1){
                    fileConfiguration.set(currentEntity.getUniqueId().toString(), currentEntity.getMetadata(GlobalValues.METATAG).get(0).asInt());
                }
                if(currentEntity.hasMetadata(GlobalValues.NOT_ENOUGH_NEAR) &&
                        currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).size() > 0 &&
                        currentEntity.getMetadata(GlobalValues.NOT_ENOUGH_NEAR).get(0).asBoolean()){
                    fileConfiguration.set(currentEntity.getUniqueId().toString(), -69);
                }
            }
        }
        try {
            fileConfiguration.save(file);
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

    public void convert(){
        SQLCache sql = new SQLCache(sm);
        if(sql.hasSqlBeenUsedBefore()){
            sm.getLogger().info("Converting SQL cache to YAML cache...");
            sql.getKeys().forEach(uuid -> write(uuid, sql.read(uuid)));
            sql.drop();
            sql.closeWithoutSaving();
        }
    }
}
