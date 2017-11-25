package uk.antiperson.stackmob.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.StackMob;

import java.io.File;

public class ConfigLoader {

    private Plugin plugin;
    private String filename;
    private File file;
    private FileConfiguration fileConfiguration;

    public ConfigLoader(StackMob plugin, String filename) {
        this.plugin = plugin;
        this.filename = filename;
    }

    public void reload() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), filename + ".yml");
        }
        if (!file.exists()) {
            plugin.saveResource(filename + ".yml", false);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        if (fileConfiguration == null) {
            reload();
        }
        return fileConfiguration;
    }

    public File getFile() {
        return file;
    }

    public void reset() {
        file.delete();
        reload();
    }

}
