package uk.antiperson.stackmob.tasks;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.hooks.MythicMobsHook;
import uk.antiperson.stackmob.compat.hooks.ProtocolLibHook;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

/**
 * Created by nathat on 25/07/17.
 */
public class TagTask extends BukkitRunnable {

    private StackMob sm;
    public TagTask(StackMob sm){
        this.sm = sm;
    }

    public void run() {
        for (Entity e : sm.worldTools.getLoadedEntities()) {
            if (!sm.config.getCustomConfig().getStringList("no-stack-worlds").contains(e.getWorld().getName())) {
                if(!(e instanceof LivingEntity)){
                    continue;
                }
                if (e.hasMetadata(GlobalValues.METATAG)) {
                    if (e.getMetadata(GlobalValues.METATAG).size() == 0 && !(e.hasMetadata(GlobalValues.NO_STACK_ALL))) {
                        e.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                    }
                    String typeString = e.getType().toString();

                    int stackSize = e.getMetadata(GlobalValues.METATAG).get(0).asInt();
                    int removeAt = sm.config.getCustomConfig().getInt("tag.remove-at");
                    if (sm.config.getCustomConfig().isString("custom." + typeString + ".tag.remove-at")) {
                        removeAt = sm.config.getCustomConfig().getInt("custom." + typeString + ".tag.remove-at");
                    }
                    if (stackSize > removeAt) {
                        String format = sm.config.getCustomConfig().getString("tag.format");
                        if (sm.config.getCustomConfig().isString("custom." + typeString + ".tag.format")) {
                              format = sm.config.getCustomConfig().getString("custom." + typeString + ".tag.format");
                        }

                        // Change if it is a mythic mob.
                        if (sm.hookManager.isHookRegistered(PluginCompat.MYTHICMOBS)) {
                            MythicMobsHook mobsHook = (MythicMobsHook) sm.hookManager.getHook(PluginCompat.MYTHICMOBS);
                            typeString = mobsHook.getDisplayName(e);
                        } else if (sm.translation.getCustomConfig().getBoolean("enabled")) {
                            typeString = "" + sm.translation.getCustomConfig().getString(e.getType().toString());
                        }

                        String formattedType = WordUtils.capitalizeFully(StringUtils.replace(StringUtils.lowerCase(typeString),"_", " "));
                        String nearlyFinal = StringUtils.replace(StringUtils.replace(StringUtils.replace(format,
                                "%bukkit_type%", e.getType().toString()),
                                "%type%", formattedType),
                                "%size%", String.valueOf(stackSize));
                        String finalString = ChatColor.translateAlternateColorCodes('&', nearlyFinal);
                        if(!finalString.equals(e.getCustomName())){
                             e.setCustomName(finalString);
                        }

                        if(sm.hookManager.isHookRegistered(PluginCompat.PROCOTOLLIB)){
                            ProtocolLibHook plh = (ProtocolLibHook) sm.hookManager.getHook(PluginCompat.PROCOTOLLIB);
                            for(Entity entity : e.getNearbyEntities(20, 20, 20)){
                                if(entity instanceof Player){
                                    plh.sendUpdatePacket((Player)entity, e);
                                }
                            }
                        }else{
                            boolean alwaysVisible = sm.config.getCustomConfig().getBoolean("tag.always-visible");
                            if (sm.config.getCustomConfig().isString("custom." + typeString + ".tag.always-visible")) {
                                alwaysVisible = sm.config.getCustomConfig().getBoolean("custom." + typeString + ".tag.always-visible");
                            }
                            e.setCustomNameVisible(alwaysVisible);
                        }
                    }
                }

            }
        }
    }
}
