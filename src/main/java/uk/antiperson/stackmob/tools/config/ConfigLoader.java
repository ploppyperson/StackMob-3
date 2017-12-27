package uk.antiperson.stackmob.tools.config;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.antiperson.stackmob.StackMob;

import java.io.File;
import java.io.IOException;

/**
 * Created by nathat on 01/06/17.
 */
public class ConfigLoader {

    public FileConfiguration fileConfiguration;
    public File file;
    private StackMob sm;
    private String filename;
    public ConfigLoader(StackMob sm, String filename){
        this.sm = sm;
        this.filename = filename;
        this.file = new File(sm.getDataFolder(), filename + ".yml");
    }

    public void reloadCustomConfig() {
        if(!file.exists()){
            sm.saveResource(filename + ".yml", false);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getCustomConfig() {
        if (fileConfiguration == null) {
            reloadCustomConfig();
        }
        return fileConfiguration;
    }

    public void generateNewVersion(){
        File oldFile = new File(sm.getDataFolder(), filename + ".old");
        try{
            FileUtils.moveFile(file, oldFile);
        }catch (IOException e){
            e.printStackTrace();
        }
        reloadCustomConfig();
    }
}
