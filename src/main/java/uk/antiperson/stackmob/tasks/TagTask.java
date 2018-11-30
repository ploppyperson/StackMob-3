package uk.antiperson.stackmob.tasks;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.hooks.MythicMobsHook;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.tools.WorldTools;

/**
 * Created by nathat on 25/07/17.
 */
public class TagTask extends BukkitRunnable {

    private StackMob sm;
    public TagTask(StackMob sm){
        this.sm = sm;
    }

    public void run() {
        MythicMobsHook mobsHook = (MythicMobsHook) sm.getHookManager().getHook(PluginCompat.MYTHICMOBS);
        for (Entity e : WorldTools.getLoadedEntities()) {
            if (!sm.getCustomConfig().getStringList("no-stack-worlds").contains(e.getWorld().getName())) {
                if(!(e instanceof LivingEntity)){
                    continue;
                }
                if (StackTools.hasValidStackData(e)) {
                    String typeString = e.getType().toString();

                    int stackSize = StackTools.getSize(e);
                    int removeAt = sm.getCustomConfig().getInt("tag.remove-at");
                    if (sm.getCustomConfig().isString("custom." + typeString + ".tag.remove-at")) {
                        removeAt = sm.getCustomConfig().getInt("custom." + typeString + ".tag.remove-at");
                    }
                    if (stackSize > removeAt) {
                        String format = sm.getCustomConfig().getString("tag.format");
                        if (sm.getCustomConfig().isString("custom." + typeString + ".tag.format")) {
                              format = sm.getCustomConfig().getString("custom." + typeString + ".tag.format");
                        }

                        // Change if it is a mythic mob.
                        if (sm.getHookManager().isHookRegistered(PluginCompat.MYTHICMOBS) && mobsHook.isMythicMob(e)) {
                            typeString = mobsHook.getDisplayName(e);
                        } else if (sm.getCustomConfig().getBoolean("tag.use-translation")) {
                            typeString = "" + sm.getTranslationConfig().getString(e.getType().toString());
                        }

                        String formattedType = WordUtils.capitalizeFully(typeString.replaceAll("[^A-Za-z0-9]", " "));
                        String nearlyFinal = StringUtils.replace(StringUtils.replace(StringUtils.replace(format,
                                "%bukkit_type%", e.getType().toString()),
                                "%type%", formattedType),
                                "%size%", String.valueOf(stackSize));
                        String finalString = ChatColor.translateAlternateColorCodes('&', nearlyFinal);
                        if(!finalString.equals(e.getCustomName())){
                             e.setCustomName(finalString);
                        }

                        if(!(sm.getHookManager().isHookRegistered(PluginCompat.PROCOTOLLIB))){
                            boolean alwaysVisible = sm.getCustomConfig().getBoolean("tag.always-visible");
                            if (sm.getCustomConfig().isString("custom." + typeString + ".tag.always-visible")) {
                                alwaysVisible = sm.getCustomConfig().getBoolean("custom." + typeString + ".tag.always-visible");
                            }
                            e.setCustomNameVisible(alwaysVisible);
                        }
                    }
                }

            }
        }
    }

}
