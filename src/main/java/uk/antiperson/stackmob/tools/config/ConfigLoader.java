package uk.antiperson.stackmob.tools.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.antiperson.stackmob.StackMob;

import java.io.File;

/**
 * Created by nathat on 01/06/17.
 */
public class ConfigLoader {

    private FileConfiguration fc;
    private File f;
    private StackMob sm;
    private String filename;
    public ConfigLoader(StackMob sm, String filename){
        this.sm = sm;
        this.filename = filename;
    }

    public void reloadCustomConfig() {
        if (f == null) {
            f = new File(sm.getDataFolder(), filename + ".yml");
        }
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

}
