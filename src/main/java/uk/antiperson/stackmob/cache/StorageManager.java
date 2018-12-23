package uk.antiperson.stackmob.cache;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.cache.storage.FlatFile;
import uk.antiperson.stackmob.cache.storage.MySQL;
import uk.antiperson.stackmob.entity.StackTools;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StorageManager {

    private StackMob sm;
    private StackStorage stackStorage;
    private Map<UUID, Integer> amountCache = new HashMap<>();
    public StorageManager(StackMob sm){
        this.sm = sm;
    }

    public void onServerEnable(){
        StorageType cacheType = StorageType.valueOf(getStackMob().getCustomConfig().getString("storage.type"));
        getStackMob().getLogger().info("Using " + cacheType.toString() + " storage method.");
        switch (cacheType){
            case MYSQL:
                stackStorage = new MySQL(this);
                break;
            case FLATFILE:
                stackStorage = new FlatFile(this);
                break;
        }

        stackStorage.loadStorage();
    }

    public void onServerDisable(){
        saveStorage();
        if(stackStorage instanceof DisableCleanup){
            ((DisableCleanup) stackStorage).onDisable();
        }
    }

    public Map<UUID, Integer> getCombinedMap(){
        Map<UUID, Integer> toSave = new HashMap<>();
        toSave.putAll(getAmountCache());
        toSave.putAll(StackTools.getEntries());
        return toSave;
    }

    public void saveStorage(){
        stackStorage.saveStorage(getCombinedMap());
    }

    public StackMob getStackMob() {
        return sm;
    }

    public StackStorage getStackStorage() {
        return stackStorage;
    }

    public Map<UUID, Integer> getAmountCache() {
        return amountCache;
    }


}
