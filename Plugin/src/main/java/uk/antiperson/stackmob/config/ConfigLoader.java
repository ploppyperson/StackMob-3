package uk.antiperson.stackmob.config;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.config.IConfigLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Created by nathat on 01/06/17.
 */
public class ConfigLoader implements IConfigLoader {

    private FileConfiguration fc;
    private File file;
    private File defaultFolder;
    private File defaultFile;
    private StackMob sm;
    private String filename;
    
    public ConfigLoader(StackMob sm, String filename){
        this.sm = sm;
        this.filename = filename;
        this.file = new File(sm.getDataFolder(), filename + ".yml");
        this.defaultFolder = new File(sm.getDataFolder() + File.separator + "defaults");
        this.defaultFile = new File(sm.getDataFolder() + File.separator + "defaults",filename + ".yml");
    }

    @Override
    public boolean check(String config, String toCheck){
        if(getCustomConfig().getStringList("no-" + config)
                .contains(toCheck)){
            return true;
        }
        if(getCustomConfig().getStringList(config).size() > 0){
            return !getCustomConfig().getStringList(config).contains(toCheck);
        }
        return false;
    }

    @Override
    public void reloadCustomConfig() {
        if(!defaultFolder.exists()){
            defaultFolder.mkdir();
        }
        if(!file.exists()){
            sm.saveResource(filename + ".yml", false);
        }
        if(!defaultFile.exists()){
            copyDefault();
        }
        fc = YamlConfiguration.loadConfiguration(file);
        if(updateConfig()){
            sm.getLogger().info("Configuration file (" + file.getName() + ") has been updated with the latest values.");
        }
    }

    @Override
    public FileConfiguration getCustomConfig() {
        if (fc == null) {
            reloadCustomConfig();
        }
        return fc;
    }

    @Override
    public File getF(){
        return file;
    }

    @Override
    public void generateNewVersion(){
        File file = new File(sm.getDataFolder(), filename + ".old");
        try{
            FileUtils.moveFile(getF(), file);
        }catch (IOException e){
            e.printStackTrace();
        }
        reloadCustomConfig();
    }

    @Override
    public void copyDefault() {
        try {
            Files.copy(is, defaultFile.toPath());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateConfig(){
        // Get the latest version of the file from the jar.
        InputStream is = sm.getResource(filename +  ".yml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        FileConfiguration includedFile = YamlConfiguration.loadConfiguration(reader);
        // Load a copy of the current file to check for later.
        FileConfiguration originalFile = YamlConfiguration.loadConfiguration(file);
        // Loop through the values of the latest version and set any that are not present.
        for(String key : includedFile.getKeys(true)){
            if(!(getCustomConfig().contains(key))){
                getCustomConfig().set(key, includedFile.get(key));
            }
        }
        // Save the changes made, copy the default file.
        if(!(getCustomConfig().saveToString().equals(originalFile.saveToString()))){
            copyDefault();
            try {
                fc.save(file);
                return true;
            }catch (IOException e){
                return false;
            }
        }
        return false;
    }
}
