package uk.antiperson.stackmob.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FlatFile extends StackStorage {

    private File file;
    private FileConfiguration fileCon;
    public FlatFile(StorageManager storageManager){
        super(storageManager, StorageType.FLATFILE);
        file = new File(storageManager.getStackMob().getDataFolder(), "storage.yml");
        reloadFile();
    }

    @Override
    public void loadCache(){
        for(String key : fileCon.getKeys(false)){
            getAmountCache().put(UUID.fromString(key), fileCon.getInt(key));
        }
    }

    @Override
    public void saveCache(){
        getFile().delete();
        reloadFile();

        saveData();
        try{
            fileCon.save(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void setValue(UUID uuid, int size){
        fileCon.set(uuid.toString(), size);
    }

    @Override
    public int getValue(UUID uuid){
        return fileCon.getInt(uuid.toString());
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
