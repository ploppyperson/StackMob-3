package uk.antiperson.stackmob.api;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import uk.antiperson.stackmob.api.cache.StorageManager;
import uk.antiperson.stackmob.api.checks.TraitManager;
import uk.antiperson.stackmob.api.compat.HookManager;
import uk.antiperson.stackmob.api.config.ConfigLoader;
import uk.antiperson.stackmob.api.entity.EntityTools;
import uk.antiperson.stackmob.api.entity.StackLogic;
import uk.antiperson.stackmob.api.entity.death.DeathManager;
import uk.antiperson.stackmob.api.entity.multiplication.DropTools;
import uk.antiperson.stackmob.api.entity.multiplication.ExperienceTools;
import uk.antiperson.stackmob.api.stick.StickTools;
import uk.antiperson.stackmob.api.tools.UpdateChecker;

import java.util.Map;
import java.util.UUID;

public interface StackMob extends Plugin {
    FileConfiguration getCustomConfig();

    ConfigLoader getConfigFile();

    HookManager getHookManager();

    StorageManager getStorageManager();

    TraitManager getTraitManager();

    DeathManager getDeathManager();

    EntityTools getTools();

    DropTools getDropTools();

    StickTools getStickTools();

    FileConfiguration getTranslationConfig();

    ConfigLoader getTranslationFile();

    ExperienceTools getExpTools();

    FileConfiguration getGeneralConfig();

    ConfigLoader getGeneralFile();

    UpdateChecker getUpdater();

    StackLogic getLogic();

    Map<UUID, Integer> getCache();
}
