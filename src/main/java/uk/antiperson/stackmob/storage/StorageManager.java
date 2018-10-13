package uk.antiperson.stackmob.storage;

import uk.antiperson.stackmob.StackMob;

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

        stackStorage.loadCache();
    }

    public void onServerDisable(){
        stackStorage.saveCache();
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
