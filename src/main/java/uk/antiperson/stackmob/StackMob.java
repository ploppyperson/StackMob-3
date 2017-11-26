package uk.antiperson.stackmob;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import uk.antiperson.stackmob.commands.CommandHandler;
import uk.antiperson.stackmob.config.Config;
import uk.antiperson.stackmob.config.ConfigLoader;
import uk.antiperson.stackmob.config.Translation;
import uk.antiperson.stackmob.listeners.ChunkListener;
import uk.antiperson.stackmob.listeners.entity.*;
import uk.antiperson.stackmob.services.*;
import uk.antiperson.stackmob.storage.Cache;
import uk.antiperson.stackmob.tasks.StackTask;
import uk.antiperson.stackmob.tasks.TagUpdater;
import uk.antiperson.stackmob.utils.BukkitVersion;

import java.time.LocalDate;

@NoArgsConstructor
// TODO: use the api instead of directly accessing the metadata values
public final class StackMob extends JavaPlugin {

    @Getter
    private static StackMob instance;

    // Config
    private Config config;
    private Translation translation;

    // Services
    private BukkitService bukkitService;
    private UpdateService updateService;
    private SupportService supportService;
    private EntityService entityService;
    private DropService dropService;

    // Storage
    private Cache cache;

    @Deprecated
    public BukkitService getBukkitService() {
        return bukkitService;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Startup messages.
        getLogger().info("StackMob version " + getDescription().getVersion() + " created by antiPerson/BaconPied");
        getLogger().info("Documentation can be found at " + getDescription().getWebsite());
        getLogger().info("GitHub repository can be found at https://github.com/Nathat23/StackMob");

        // Easter eggs.
        if (LocalDate.now().getDayOfYear() >= 357) {
            getLogger().info("Merry Christmas!");
        }

        // Bukkit version warning.
        if (BukkitVersion.getVersion() == BukkitVersion.UNSUPPORTED) {
            getLogger().warning("This bukkitService version (" + Bukkit.getVersion() + ") is not currently supported!");
            getLogger().warning("New Minecraft features are not supported, so some issues may occur!");
        }

        // Loads configuration file into memory, and if not found, file is copied from the jar file.
        config = new Config(this);
        config.reload();
        translation = new Translation(this);
        translation.reload();

        // Init services
        bukkitService = new BukkitService(this);
        updateService = new UpdateService(this, 29999);
        supportService = new SupportService(config, getServer().getPluginManager());
        entityService = new EntityService(config, supportService, bukkitService);
        dropService = new DropService(config, bukkitService);

        // Send metrics and check for updates.
        new Metrics(this);
        updateService.checkUpdate().thenAccept(message -> getLogger().info(message));

        // Load the cache data.
        cache = new Cache(this);
        long millis = System.currentTimeMillis();
        cache.loadCache();
        getLogger().info("Loaded data, took " + millis + "ms");

        // Start tasks.
        bukkitService.runTaskTimer(new TagUpdater(config, translation, bukkitService, supportService), 0, 5);
        bukkitService.runTaskTimer(new StackTask(config, entityService, bukkitService), 0, config.get().getInt("task-delay"));

        // Register event listeners.
        registerListeners();

        // Register commands.
        getCommand("sm").setExecutor(new CommandHandler(getDescription(), config, updateService, cache, bukkitService));
    }

    @Override
    public void onDisable() {
        // Cancel any running task before saving data.
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);

        // Save the cache so entity amounts aren't lost on restarts.
        if (cache != null) {
            cache.saveCache();
        }
    }

    // Needs a full restart to be reloaded
    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Essential events/tasks that are needed for the plugin to function correctly.
        pluginManager.registerEvents(new SpawnListener(config, supportService, entityService, bukkitService), this);
        pluginManager.registerEvents(new DeathListener(config, entityService, dropService, bukkitService), this);
        pluginManager.registerEvents(new ChunkListener(config, cache, bukkitService), this);

        // Events that are not required for the plugin to function, however they make a better experience.
        if (config.get().getBoolean("multiply.creeper-explosion")) {
            pluginManager.registerEvents(new ExplodeListener(), this);
        }
        if (config.get().getBoolean("multiply.chicken-eggs")) {
            pluginManager.registerEvents(new ItemDropListener(dropService), this);
        }
        if (config.get().getBoolean("divide-on.sheep-dye")) {
            pluginManager.registerEvents(new SheepDyeListener(config, entityService, bukkitService), this);
        }
        if (config.get().getBoolean("divide-on.breed")) {
            pluginManager.registerEvents(new InteractListener(config, entityService, bukkitService), this);
        }
        if (config.get().getBoolean("multiply-damage-done")) {
            pluginManager.registerEvents(new DamageMultiplierListener(), this);
        }
        if (config.get().getBoolean("multiply-damage-received.enabled")) {
            pluginManager.registerEvents(new DamageReceivedListener(config), this);
        }
        if (config.get().getBoolean("no-targeting.enabled")) {
            pluginManager.registerEvents(new EntityTargetListener(config), this);
        }
        if (config.get().getBoolean("divide-on.tame")) {
            pluginManager.registerEvents(new TameListener(bukkitService, entityService), this);
        }

        pluginManager.registerEvents(new ShearListener(config, dropService, entityService, bukkitService), this);
        if (BukkitVersion.isAtLeast(BukkitVersion.V1_9)) {
            pluginManager.registerEvents(new BreedListener(bukkitService), this);
        }
    }

}
