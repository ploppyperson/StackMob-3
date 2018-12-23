package uk.antiperson.stackmob.cache;

import uk.antiperson.stackmob.StackMob;

public abstract class StackStorage implements StorageMethod {

    private StorageManager storageManager;
    public StackStorage(StorageManager storageManager){
        this.storageManager = storageManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public StackMob getStackMob(){
        return storageManager.getStackMob();
    }

}
