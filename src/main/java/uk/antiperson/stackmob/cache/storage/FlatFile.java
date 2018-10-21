package uk.antiperson.stackmob.cache.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.antiperson.stackmob.cache.StackStorage;
import uk.antiperson.stackmob.cache.StorageManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class FlatFile extends StackStorage {

    private File file;
    private FileConfiguration fileCon;
    public FlatFile(StorageManager storageManager){
        super(storageManager);
        file = new File(storageManager.getStackMob().getDataFolder(), "cache.yml");
        reloadFile();
    }

    @Override
    public void loadStorage(){
        for(String key : fileCon.getKeys(false)){
            getAmountCache().put(UUID.fromString(key), fileCon.getInt(key));
        }
    }

    @Override
    public void saveStorage(){
        getFile().delete();
        reloadFile();

        for(Map.Entry<UUID, Integer> entry : getAmountCache().entrySet()){
            fileCon.set(entry.getKey().toString(), entry.getValue());
        }

        try{
            fileCon.save(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void reloadFile(){
        fileCon = YamlConfiguration.loadConfiguration(file);
    }

    private File getFile() {
        return file;
    }

    private FileConfiguration getFileCon() {
        return fileCon;
    }
}
