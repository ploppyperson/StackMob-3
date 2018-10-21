package uk.antiperson.stackmob.cache;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.cache.storage.FlatFile;
import uk.antiperson.stackmob.cache.storage.MySQL;
import uk.antiperson.stackmob.cache.storage.Sqlite;

public class StorageManager {

    private StackMob sm;
    private StorageType cacheType;
    private StackStorage stackStorage;
    public StorageManager(StackMob sm){
        this.sm = sm;
    }

    public void onServerEnable(){
        cacheType = StorageType.valueOf(getStackMob().getCustomConfig().getString("storage.type"));
        getStackMob().getLogger().info("Using " + cacheType.toString() + " storage method.");
        switch (cacheType){
            case MYSQL:
                stackStorage = new MySQL(this);
                break;
            case SQLITE:
                stackStorage = new Sqlite(this);
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

    public void saveStorage(){
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
