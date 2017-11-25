package uk.antiperson.stackmob.tasks;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.ConfigLoader;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.SupportService;

import static uk.antiperson.stackmob.utils.StringUtils.toTitleCase;

@AllArgsConstructor
public class TagUpdater extends BukkitRunnable {

    private ConfigLoader config;
    private ConfigLoader translation;
    private BukkitService bukkitService;
    private SupportService supportService;

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            if (config.get().getStringList("no-stack-worlds").contains(world.getName())) {
                continue;
            }
            for (Entity entity : world.getLivingEntities()) {
                if (!entity.hasMetadata(GlobalValues.METATAG)) {
                    continue;
                }
                if (entity.getMetadata(GlobalValues.METATAG).size() == 0) {
                    bukkitService.setMetadata(entity, GlobalValues.METATAG, 1);
                }
                String typeString = entity.getType().toString();

                int removeAt = config.get().getInt("tag.remove-at");
                if (config.get().isString("custom." + typeString + ".tag.remove-at")) {
                    removeAt = config.get().getInt("custom." + typeString + ".tag.remove-at");
                }

                if (entity.getMetadata(GlobalValues.METATAG).get(0).asInt() <= removeAt) {
                    continue;
                }

                String format = config.get().getString("tag.format");
                if (config.get().isString("custom." + typeString + ".tag.format")) {
                    format = config.get().getString("custom." + typeString + ".tag.format");
                }

                // Change if it is a mythic mob.
                if (supportService.isMythicMob(entity)) {
                    typeString = supportService.getMythicName(entity);
                } else if (translation.get().getBoolean("enabled")) {
                    typeString = "" + translation.get().getString(entity.getType().toString());
                }

                String formattedType = toTitleCase(typeString.toLowerCase().replace("_", " "));
                String nearlyFinal = format.replace("%size%", entity.getMetadata(GlobalValues.METATAG).get(0).asString())
                        .replace("%type%", formattedType)
                        .replace("%bukkit_type%", entity.getType().toString());
                String finalString = ChatColor.translateAlternateColorCodes('&', nearlyFinal);
                entity.setCustomName(finalString);

                boolean alwaysVisible = config.get().getBoolean("tag.always-visible");
                if (config.get().isString("custom." + typeString + ".tag.always-visible")) {
                    alwaysVisible = config.get().getBoolean("custom." + typeString + ".tag.always-visible");
                }
                entity.setCustomNameVisible(alwaysVisible);
            }
        }
    }

}
