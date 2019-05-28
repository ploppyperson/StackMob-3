package uk.antiperson.stackmob.cache;

import uk.antiperson.stackmob.StackMobPlugin;

public abstract class StackStorage implements uk.antiperson.stackmob.api.cache.StackStorage {

    private StorageManager storageManager;
    public StackStorage(StorageManager storageManager){
        this.storageManager = storageManager;
    }

    @Override
    public StorageManager getStorageManager() {
        return storageManager;
    }

    @Override
    public StackMobPlugin getStackMob(){
        return storageManager.getStackMob();
    }

}
