package uk.antiperson.stackmob.tools.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.antiperson.stackmob.StackMob;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Created by nathat on 01/06/17.
 */
public class MainConfiguration {

    private FileConfiguration fc;
    private File f;
    private StackMob sm;
    public MainConfiguration(StackMob sm){
        this.sm = sm;
    }

    public void reloadCustomConfig() {
        if (f == null) {
            f = new File(sm.getDataFolder(), "config.yml");
        }
        if(!f.exists()){
            sm.saveResource("config.yml", false);
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
