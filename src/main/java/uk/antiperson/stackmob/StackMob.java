package uk.antiperson.stackmob;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import uk.antiperson.stackmob.events.chunk.LoadEvent;
import uk.antiperson.stackmob.events.chunk.UnloadEvent;
import uk.antiperson.stackmob.events.entity.*;
import uk.antiperson.stackmob.tasks.StackTask;
import uk.antiperson.stackmob.tasks.TagTask;
import uk.antiperson.stackmob.tools.*;
import uk.antiperson.stackmob.tools.config.CacheFile;
import uk.antiperson.stackmob.tools.config.ConfigFile;
import uk.antiperson.stackmob.tools.config.ConfigLoader;
import uk.antiperson.stackmob.tools.config.TranslationFile;
import uk.antiperson.stackmob.tools.extras.GlobalValues;
import uk.antiperson.stackmob.tools.plugin.PluginSupport;

import java.time.LocalDate;


/**
 * Created by nathat on 23/07/17.
 */
public class StackMob extends JavaPlugin {

    private int versionId = 0;
    public ConfigFile config = new ConfigFile(this);
    public TranslationFile translation = new TranslationFile(this);
    public EntityTools checks = new EntityTools(this);
    public CacheFile cache = new CacheFile(this);
    public DropTools dropTools = new DropTools(this);
    public PluginSupport pluginSupport = new PluginSupport(this);
    public UpdateChecker updater = new UpdateChecker(this);


    @Override
    public void onEnable(){
        // Startup messages
        getLogger().info("StackMob v" + getDescription().getVersion() + " created by antiPerson/BaconPied");
        getLogger().info("Documentation can be found at " + getDescription().getWebsite());
        getLogger().info("GitHub repository can be found at " + GlobalValues.GITHUB);

        // Set version id, but if not supported, warn.
        setVersionId();
        if(getVersionId() == 0){
            getLogger().warning("A bukkit version that is not supported has been detected! (" + Bukkit.getBukkitVersion() + ")");
            getLogger().warning("The features of this version are not supported, so some issues may occur!");
        }

        // Loads configuration file into memory, and if not found, file is copied from the jar file.
        config.reloadCustomConfig();
        translation.reloadCustomConfig();

        if(config.getCustomConfig().isBoolean("plugin.loginupdatechecker")){
            getLogger().info("An old version of the configuration file has been detected!");
            getLogger().info("A new one will be generated and the old one will be renamed to config.old");
            config.generateNewVersion();
        }

        if(config.getCustomConfig().getBoolean("tag.show-player-nearby.enabled")){
            if(!pluginSupport.isProtocolSupportEnabled()){
                getLogger().info("ProtocolLib is required for certain features, but it cannot be found!");
                getLogger().info("These feature(s) will not work until ProtocolLib is installed.");
            }
        }

        // Load the cache.
        getLogger().info("Loading cached entities...");
        cache.loadCache();

        // Essential events/tasks that are needed for the plugin to function correctly.
        getLogger().info("Registering events...");
        registerEssentialEvents();

        // Events that are not required for the plugin to function, however they make a better experience.
        registerNotEssentialEvents();

        new Metrics(this);

        getLogger().info(updater.updateString());

        if(LocalDate.now().getDayOfYear() >= 357 && LocalDate.now().getDayOfYear() < 3){
            getLogger().info("Merry Christmas and Happy new year!");
        }

    }


    @Override
    public void onDisable(){
        getLogger().info("Cancelling currently running tasks...");
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("Saving entity amount cache...");
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
        }else if(Bukkit.getVersion().contains("1.13")){
            versionId = 6;
        }
    }

    public int getVersionId(){
        return versionId;
    }

    private void registerEssentialEvents(){
        getServer().getPluginManager().registerEvents(new SpawnEvent(this), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new LoadEvent(this), this);
        getServer().getPluginManager().registerEvents(new UnloadEvent(this), this);
        getCommand("sm").setExecutor(new Commands(this));
        new StackTask(this).runTaskTimer(this, 0, config.getCustomConfig().getInt("task-delay"));
        new TagTask(this).runTaskTimer(this, 0, 5);
    }

    private void registerNotEssentialEvents(){
        if(config.getCustomConfig().getBoolean("multiply.creeper-explosion")){
            getServer().getPluginManager().registerEvents(new ExplodeEvent(this), this);
        }
        if(config.getCustomConfig().getBoolean("multiply.chicken-eggs")){
            getServer().getPluginManager().registerEvents(new ItemDrop(this), this);
        }
        if(config.getCustomConfig().getBoolean("divide-on.sheep-dye")) {
            getServer().getPluginManager().registerEvents(new DyeEvent(this), this);
        }
        if(config.getCustomConfig().getBoolean("divide-on.breed")){
            getServer().getPluginManager().registerEvents(new InteractEvent(this), this);
        }
        if(config.getCustomConfig().getBoolean("multiply.small-slimes")) {
            getServer().getPluginManager().registerEvents(new SlimeEvent(this), this);
        }
        if(config.getCustomConfig().getBoolean("multiply-damage-done")){
            getServer().getPluginManager().registerEvents(new DealtDamageEvent(this), this);
        }
        if(config.getCustomConfig().getBoolean("multiply-damage-received.enabled")){
            getServer().getPluginManager().registerEvents(new ReceivedDamageEvent(this), this);
        }
        if(config.getCustomConfig().getBoolean("no-targeting.enabled")){
            getServer().getPluginManager().registerEvents(new TargetEvent(this), this);
        }
        if(config.getCustomConfig().getBoolean("divide-on.tame")){
            getServer().getPluginManager().registerEvents(new TameEvent(this), this);
        }
        getServer().getPluginManager().registerEvents(new ShearEvent(this), this);
        if(getVersionId() > 2){
            getServer().getPluginManager().registerEvents(new BreedEvent(this), this);
        }
    }
}
