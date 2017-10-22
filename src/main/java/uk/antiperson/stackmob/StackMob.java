package uk.antiperson.stackmob;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import uk.antiperson.stackmob.events.chunk.ChunkLoad;
import uk.antiperson.stackmob.events.chunk.ChunkUnload;
import uk.antiperson.stackmob.events.entity.*;
import uk.antiperson.stackmob.tasks.StackTask;
import uk.antiperson.stackmob.tasks.TagUpdater;
import uk.antiperson.stackmob.tools.DropTools;
import uk.antiperson.stackmob.tools.EntityTools;
import uk.antiperson.stackmob.tools.PluginSupport;
import uk.antiperson.stackmob.tools.Update;
import uk.antiperson.stackmob.tools.config.Cache;
import uk.antiperson.stackmob.tools.config.ConfigLoader;
import uk.antiperson.stackmob.tools.extras.GlobalValues;


/**
 * Created by nathat on 23/07/17.
 */
public class StackMob extends JavaPlugin {

    /*
    TODO: Slime multiplication with kill-all/kill-step
     */

    private int versionId = 0;
    public ConfigLoader config = new ConfigLoader(this, "config");
    public ConfigLoader translation = new ConfigLoader(this, "lang");
    public EntityTools checks = new EntityTools(this);
    public Cache cache = new Cache(this);
    public DropTools dropTools = new DropTools(this);
    public PluginSupport pluginSupport = new PluginSupport(this);
    public Update updater = new Update(this);


    @Override
    public void onEnable(){
        // Startup messages
        getLogger().info("StackMob version " + getDescription().getVersion() + " created by antiPerson/BaconPied");
        getLogger().info("Documentation can be found at " + getDescription().getWebsite());
        getLogger().info("GitHub repository can be found at " + GlobalValues.GITHUB);

        // Set version id, but if not supported, warn.
        setVersionId();
        if(getVersionId() == 0){
            getLogger().warning("This bukkit version (" + Bukkit.getVersion() + ") is not currently supported!");
            getLogger().warning("New Minecraft features are not supported, so some issues may occur!");
        }

        // Loads configuration file into memory, and if not found, file is copied from the jar file.
        config.reloadCustomConfig();
        translation.reloadCustomConfig();

        // Load the cache.
        cache.loadCache();

        // Essential events/tasks that are needed for the plugin to function correctly.
        registerEssentialEvents();

        // Events that are not required for the plugin to function, however they make a better experience.
        registerNotEssentialEvents();

        new Metrics(this);

        getLogger().info(updater.updateString());

    }


    @Override
    public void onDisable(){
        getServer().getScheduler().cancelTasks(this);
        // Save the cache so entity amounts aren't lost on restarts
        cache.saveCache();
    }

    // Server version detection, if version isn't currently supported, then versionId is 0.
    private void setVersionId(){
        if(Bukkit.getVersion().contains("1.8")){
            versionId = 1;
        }else if(Bukkit.getVersion().contains("1.9")){
            versionId = 2;
        }else if(Bukkit.getVersion().contains("1.10")){
            versionId = 3;
        }else if(Bukkit.getVersion().contains("1.11")){
            versionId = 4;
        }else if(Bukkit.getVersion().contains("1.12")){
            versionId = 5;
        }
    }

    public int getVersionId(){
        return versionId;
    }

    private void registerEssentialEvents(){
        getServer().getPluginManager().registerEvents(new Spawn(this), this);
        getServer().getPluginManager().registerEvents(new Death(this), this);
        getServer().getPluginManager().registerEvents(new ChunkLoad(this), this);
        getServer().getPluginManager().registerEvents(new ChunkUnload(this), this);
        new TagUpdater(this).runTaskTimer(this, 0, 5);
        new StackTask(this).runTaskTimer(this, 0, config.getCustomConfig().getInt("task-delay"));
        getCommand("sm").setExecutor(new Commands(this));
    }

    private void registerNotEssentialEvents(){
        if(config.getCustomConfig().getBoolean("multiply.creeper-explosion")){
            getServer().getPluginManager().registerEvents(new Explode(this), this);
        }
        if(config.getCustomConfig().getBoolean("multiply.chicken-eggs")){
            getServer().getPluginManager().registerEvents(new ItemDrop(this), this);
        }
        if(config.getCustomConfig().getBoolean("divide-on.sheep-dye")) {
            getServer().getPluginManager().registerEvents(new SheepDye(this), this);
        }
        if(config.getCustomConfig().getBoolean("divide-on.breed")){
            getServer().getPluginManager().registerEvents(new Interact(this), this);
        }
        if(config.getCustomConfig().getBoolean("multiply-damage-done")){
            getServer().getPluginManager().registerEvents(new DamgeDelt(this), this);
        }
        if(config.getCustomConfig().getBoolean("multiply-damage-received.enabled")){
            getServer().getPluginManager().registerEvents(new DamageReceived(this), this);
        }
        if(config.getCustomConfig().getBoolean("no-targeting.enabled")){
            getServer().getPluginManager().registerEvents(new EntityTarget(this), this);
        }
        if(config.getCustomConfig().getBoolean("divide-on.tame")){
            getServer().getPluginManager().registerEvents(new Tame(this), this);
        }
        getServer().getPluginManager().registerEvents(new Shear(this), this);
        if(getVersionId() > 1){
            getServer().getPluginManager().registerEvents(new Breed(this), this);
        }
    }
}
