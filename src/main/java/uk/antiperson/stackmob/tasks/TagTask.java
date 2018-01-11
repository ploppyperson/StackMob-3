package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;
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
        for (World w : Bukkit.getWorlds()) {
            if (!sm.config.getCustomConfig().getStringList("no-stack-worlds").contains(w.getName())) {
                for (Entity e : w.getLivingEntities()) {
                    if (e.hasMetadata(GlobalValues.METATAG)) {
                        if (e.getMetadata(GlobalValues.METATAG).size() == 0) {
                            e.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                        }
                        String typeString = e.getType().toString();

                        int removeAt = sm.config.getCustomConfig().getInt("tag.remove-at");
                        if (sm.config.getCustomConfig().isString("custom." + typeString + ".tag.remove-at")) {
                            removeAt = sm.config.getCustomConfig().getInt("custom." + typeString + ".tag.remove-at");
                        }
                        if (e.getMetadata(GlobalValues.METATAG).get(0).asInt() > removeAt) {
                            String format = sm.config.getCustomConfig().getString("tag.format");
                            if (sm.config.getCustomConfig().isString("custom." + typeString + ".tag.format")) {
                                format = sm.config.getCustomConfig().getString("custom." + typeString + ".tag.format");
                            }

                            // Change if it is a mythic mob.
                            if (sm.pluginSupport.getMythicSupport() != null && sm.pluginSupport.getMythicSupport().isMythicMob(e)) {
                                typeString = sm.pluginSupport.getMythicSupport().getMythicMobs().getMythicMobInstance(e).getType().getInternalName();
                            } else if (sm.translation.getCustomConfig().getBoolean("enabled")) {
                                typeString = "" + sm.translation.getCustomConfig().getString(e.getType().toString());
                            }

                            String formattedType = toTitleCase(typeString.toLowerCase().replace("_", " "));
                            String nearlyFinal = format.replace("%size%", e.getMetadata(GlobalValues.METATAG).get(0).asString())
                                    .replace("%type%", formattedType)
                                    .replace("%bukkit_type%", e.getType().toString());
                            String finalString = ChatColor.translateAlternateColorCodes('&', nearlyFinal);
                            e.setCustomName(finalString);

                            if(sm.config.getCustomConfig().getBoolean("tag.show-player-nearby.enabled") && sm.pluginSupport.isProtocolSupportEnabled() && sm.getVersionId() > 1){
                                Bukkit.getOnlinePlayers().forEach(player -> sm.pluginSupport.getProtocolSupport().sendUpdatePacket(player, e));
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

    private String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
