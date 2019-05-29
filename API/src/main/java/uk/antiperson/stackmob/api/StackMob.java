package uk.antiperson.stackmob.api;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.api.cache.IStorageManager;
import uk.antiperson.stackmob.api.checks.ITraitManager;
import uk.antiperson.stackmob.api.compat.IHookManager;
import uk.antiperson.stackmob.api.config.IConfigLoader;
import uk.antiperson.stackmob.api.entity.IEntityTools;
import uk.antiperson.stackmob.api.entity.IStackLogic;
import uk.antiperson.stackmob.api.entity.death.IDeathManager;
import uk.antiperson.stackmob.api.entity.multiplication.IDropTools;
import uk.antiperson.stackmob.api.entity.multiplication.IExperienceTools;
import uk.antiperson.stackmob.api.stick.IStickTools;
import uk.antiperson.stackmob.api.tools.IUpdateChecker;

import java.util.Map;
import java.util.UUID;

public interface StackMob extends Plugin {
    FileConfiguration getCustomConfig();

    IConfigLoader getConfigFile();

    IHookManager getHookManager();

    IStorageManager getStorageManager();

    ITraitManager getTraitManager();

    IDeathManager getDeathManager();

    IEntityTools getTools();

    IDropTools getDropTools();

    IStickTools getStickTools();

    FileConfiguration getTranslationConfig();

    IConfigLoader getTranslationFile();

    IExperienceTools getExpTools();

    FileConfiguration getGeneralConfig();

    IConfigLoader getGeneralFile();

    IUpdateChecker getUpdater();

    IStackLogic getLogic();

    Map<UUID, Integer> getCache();
}
