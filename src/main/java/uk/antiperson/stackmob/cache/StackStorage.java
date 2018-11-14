package uk.antiperson.stackmob.cache;

import uk.antiperson.stackmob.entity.StackTools;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class StackStorage implements StorageMethod {

    private StorageManager storageManager;
    private ConcurrentHashMap<UUID, Integer> amountCache = new ConcurrentHashMap<>();
    public StackStorage(StorageManager storageManager){
        this.storageManager = storageManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public ConcurrentHashMap<UUID, Integer> getAmountCache() {
        return amountCache;
    }

    public void cacheWorldData(){
        amountCache.putAll(StackTools.getEntries());
    }


}
