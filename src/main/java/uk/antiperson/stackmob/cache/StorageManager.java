package uk.antiperson.stackmob.cache;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.cache.storage.FlatFile;

public class StorageManager {

    private StackMob sm;
    private StorageType cacheType;
    private StackStorage stackStorage;
    public StorageManager(StackMob sm){
        this.sm = sm;
    }

    public void onServerEnable(){
        //cacheType = StorageType.valueOf(sm.config.getCustomConfig().getString("caching.type"));
        cacheType = StorageType.FLATFILE;
        stackStorage = new FlatFile(this);

        stackStorage.loadStorage();
    }

    public void onServerDisable(){
        stackStorage.cacheWorldData();
        stackStorage.saveStorage();
    }

    public StackMob getStackMob() {
        return sm;
    }

    public StackStorage getStackStorage() {
        return stackStorage;
    }

    public StorageType getCacheType() {
        return cacheType;
    }
}
