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

    public FileConfiguration fc;
    public File f;
    private StackMob sm;
    private String filename;
    public ConfigLoader(StackMob sm, String filename){
        this.sm = sm;
        this.filename = filename;
        this.f = new File(sm.getDataFolder(), filename + ".yml");
    }

    public void reloadCustomConfig() {
        if(!f.exists()){
            sm.saveResource(filename + ".yml", false);
        }
        fc = YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getCustomConfig() {
        if (fc == null) {
            reloadCustomConfig();
        }
        return fc;
    }

    public File getF(){
        return f;
    }

    public void generateNewVersion(){
        File file = new File(sm.getDataFolder(), filename + ".old");
        try{
            FileUtils.moveFile(getF(), file);
        }catch (IOException e){
            e.printStackTrace();
        }
        reloadCustomConfig();
    }
}
