package uk.antiperson.stackmob.cache;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class StackStorage implements StorageMethod {

    private StorageManager storageManager;
    private HashMap<UUID, Integer> amountCache = new HashMap<>();
    public StackStorage(StorageManager storageManager){
        this.storageManager = storageManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public HashMap<UUID, Integer> getAmountCache() {
        return amountCache;
    }

    public void cacheWorldData(){
        for(World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getLivingEntities()) {
                if (entity instanceof Monster) {
                    continue;
                }
                if (GeneralTools.hasValidMetadata(entity)) {
                    int stackSize = entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
                    amountCache.put(entity.getUniqueId(), stackSize);
                }
            }
        }
    }


}
