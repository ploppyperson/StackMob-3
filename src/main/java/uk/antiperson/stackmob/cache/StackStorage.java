package uk.antiperson.stackmob.cache;

import java.util.HashMap;
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
        amountCache.putAll(storageManager.getAmounts());
    }


}
