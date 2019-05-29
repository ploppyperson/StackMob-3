package uk.antiperson.stackmob.cache;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.cache.IStorageManager;
import uk.antiperson.stackmob.cache.storage.FlatFile;
import uk.antiperson.stackmob.cache.storage.MySQL;
import uk.antiperson.stackmob.api.cache.DisableCleanup;
import uk.antiperson.stackmob.api.cache.StorageType;
import uk.antiperson.stackmob.api.entity.StackTools;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class StorageManager implements IStorageManager {

    private StackMob sm;
    private StackStorage stackStorage;
    private Map<UUID, Integer> amountCache = new HashMap<>();
    public StorageManager(StackMob sm){
        this.sm = sm;
    }

    @Override
    public void onServerEnable(){
        StorageType cacheType = StorageType.valueOf(getStackMob().getCustomConfig().getString("storage.type", "FLATFILE").toUpperCase());
        getStackMob().getLogger().info("Using " + cacheType.toString() + " storage method.");
        switch (cacheType){
            case MYSQL:
                stackStorage = new MySQL(this);
                break;
            case FLATFILE:
                stackStorage = new FlatFile(this);
                break;
            default:
                sm.getLogger().log(Level.SEVERE, "Invalid storage type. Please check configuration.");
        }

        stackStorage.loadStorage();
    }

    @Override
    public void onServerDisable(){
        saveStorage();
        if(stackStorage instanceof DisableCleanup){
            ((DisableCleanup) stackStorage).onDisable();
        }
    }

    @Override
    public Map<UUID, Integer> getCombinedMap(){
        Map<UUID, Integer> persistent = StackTools.getPersistentEntries();
        Map<UUID, Integer> toSave = new HashMap<>(getAmountCache().size() + persistent.size());
        toSave.putAll(getAmountCache());
        toSave.putAll(persistent);
        return toSave;
    }

    @Override
    public void saveStorage(){
        stackStorage.saveStorage(getCombinedMap());
    }

    @Override
    public StackMob getStackMob() {
        return sm;
    }

    @Override
    public StackStorage getStackStorage() {
        return stackStorage;
    }

    @Override
    public Map<UUID, Integer> getAmountCache() {
        return amountCache;
    }


}
