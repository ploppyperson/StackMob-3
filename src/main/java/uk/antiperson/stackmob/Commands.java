package uk.antiperson.stackmob;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.UUID;


public class Commands implements CommandExecutor {

    private StackMob sm;

    public Commands(StackMob sm) {
        this.sm = sm;
    }

    private final String pluginTag = ChatColor.LIGHT_PURPLE + "StackMob " + ChatColor.GRAY + ">> ";
    private final String errorTag = ChatColor.RED + "" + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED;
    private final String noPerm = pluginTag + errorTag +
            "You do not have the permission to perform this command! If you believe this is in error, contact the server administration.";

    @Override
    // the nest of doom.
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if(sender.hasPermission("StackMob.*") || sender.hasPermission("StackMob.Admin")) {
            if (args.length == 0) {
                sender.sendMessage(pluginTag + ChatColor.GOLD + "Plugin commands:");
                sender.sendMessage(ChatColor.AQUA + "/sm spawnstack [size] [entity type] " + ChatColor.GREEN + "Spawns a new pre-stacked entity.");
                sender.sendMessage(ChatColor.AQUA + "/sm remove [radius] " + ChatColor.GREEN + "Removes all of the stacked entities loaded in the specified radius.");
                sender.sendMessage(ChatColor.AQUA + "/sm removeall " + ChatColor.GREEN + "Removes all of the stacked entities loaded.");
                sender.sendMessage(ChatColor.AQUA + "/sm stick " + ChatColor.GREEN + "Gives you the stick of stacking.");
                sender.sendMessage(ChatColor.AQUA + "/sm stats " + ChatColor.GREEN + "Displays entity statistics.");
                sender.sendMessage(ChatColor.AQUA + "/sm reload " + ChatColor.GREEN + "Reloads the configuration file.");
                sender.sendMessage(ChatColor.AQUA + "/sm reset " + ChatColor.GREEN + "Resets the configuration file.");
                sender.sendMessage(ChatColor.AQUA + "/sm check " + ChatColor.GREEN + "Checks for version updates.");
                sender.sendMessage(ChatColor.AQUA + "/sm update " + ChatColor.GREEN + "Downloads the latest version.");
                sender.sendMessage(ChatColor.AQUA + "/sm about " + ChatColor.GREEN + "Shows plugin information.");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("about")) {
                    sender.sendMessage(pluginTag + ChatColor.GOLD + "StackMob v" + sm.getDescription().getVersion() + " by antiPerson and contributors.");
                    sender.sendMessage(pluginTag + ChatColor.YELLOW + "Find out more at " + sm.getDescription().getWebsite());
                    sender.sendMessage(pluginTag + ChatColor.YELLOW + "Find the source code at " + GlobalValues.GITHUB);
                    sender.sendMessage(pluginTag + ChatColor.YELLOW + "Has this plugin helped your server? Please leave a review!");
                } else if (args[0].equalsIgnoreCase("reset")) {
                    sm.config.getF().delete();
                    sm.config.reloadCustomConfig();
                    sender.sendMessage(pluginTag + ChatColor.GREEN + "The configuration has been reset and reloaded.");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    sm.config.reloadCustomConfig();
                    sender.sendMessage(pluginTag + ChatColor.GREEN + "The configuration has been reloaded.");
                } else if (args[0].equalsIgnoreCase("removeall")) {
                    int counter = 0;
                    for (World world : Bukkit.getWorlds()) {
                        for (Entity entity : world.getLivingEntities()) {
                            if (entity.hasMetadata(GlobalValues.METATAG)) {
                                counter++;
                                entity.remove();
                            }
                        }
                    }
                    sender.sendMessage(pluginTag + ChatColor.GREEN + "A total of " + counter + " entities were removed.");
                } else if (args[0].equalsIgnoreCase("check")) {
                    sender.sendMessage(pluginTag + ChatColor.GOLD + sm.updater.updateString());
                } else if (args[0].equalsIgnoreCase("update")) {
                    sender.sendMessage(pluginTag + ChatColor.GOLD + sm.updater.update());
                } else if (args[0].equalsIgnoreCase("stats")) {
                    int stackedCount = 0;
                    int stackedTotal = 0;
                    for (World world : Bukkit.getWorlds()) {
                        for (Entity entity : world.getLivingEntities()) {
                            if (entity.hasMetadata(GlobalValues.METATAG)) {
                                stackedCount = stackedCount + 1;
                                stackedTotal = stackedTotal + entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
                            }
                        }
                    }

                    int stackedCount1 = 0;
                    int stackedTotal1 = 0;
                    if (sender instanceof Player) {
                        for (Entity entity : ((Player) sender).getLocation().getChunk().getEntities()) {
                            if (entity.hasMetadata(GlobalValues.METATAG)) {
                                stackedCount1 = stackedCount1 + 1;
                                stackedTotal1 = stackedTotal1 + entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
                            }
                        }
                    }

                    int cacheTotal = 0;
                    for (UUID uuid : sm.cache.amountCache.keySet()) {
                        if (sm.cache.amountCache.get(uuid) > 0) {
                            cacheTotal = cacheTotal + sm.cache.amountCache.get(uuid);
                        }
                    }


                    sender.sendMessage(pluginTag + ChatColor.GOLD + "Entity stacking statistics:");
                    sender.sendMessage(ChatColor.YELLOW + "Loaded entities: " + ChatColor.GREEN + stackedCount + " (" + stackedTotal + " stacked.) "
                            + ChatColor.YELLOW + "Loaded entities (this chunk): " + ChatColor.GREEN + stackedCount1 + " (" + stackedTotal1 + " stacked.) ");
                    sender.sendMessage(ChatColor.YELLOW + "Cached entities: " + ChatColor.GREEN + sm.cache.amountCache.size() + " (" + cacheTotal + " stacked.) ");
                } else if (args[0].equalsIgnoreCase("stick")){
                    if (sender instanceof Player) {
                        sm.stickTools.giveStackingStick((Player) sender);
                    } else {
                        sender.sendMessage(pluginTag + errorTag +
                                "You need to be a player to do this!");
                    }
                } else {
                    sender.sendMessage(pluginTag + errorTag +
                            "Incorrect command parameters!");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove")) {
                    if (sender instanceof Player) {
                        Integer numb = Integer.valueOf(args[1]);
                        if (numb != null) {
                            int counter = 0;
                            for (Entity entity : ((Player) sender).getNearbyEntities(numb, numb, numb)) {
                                if (entity.hasMetadata(GlobalValues.METATAG)) {
                                    entity.remove();
                                    counter++;
                                }
                            }
                            sender.sendMessage(pluginTag + ChatColor.GREEN + "A total of " + counter + " entities were removed.");
                        } else {
                            sender.sendMessage(pluginTag + errorTag +
                                    "Invalid number format!");
                        }
                    } else {
                        sender.sendMessage(pluginTag + errorTag +
                                "You need to be a player to do this!");
                    }

                } else {
                    sender.sendMessage(pluginTag + errorTag +
                            "Incorrect command parameters!");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("spawnstack")) {

                    if (sender instanceof Player) {
                        Integer numb;
                        try {
                            numb = Integer.valueOf(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(pluginTag + errorTag +
                                    "Invalid number format!");
                            return false;
                        }
                        boolean contains = false;
                        for (EntityType type : EntityType.values()) {
                            if (args[2].equalsIgnoreCase(type.toString())) {
                                contains = true;
                            }
                        }
                        if (contains) {
                            Entity newEntity = ((Player) sender).getWorld().spawnEntity(((Player) sender).getLocation(), EntityType.valueOf(args[2].toUpperCase()));
                            newEntity.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
                            newEntity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, numb));
                            sender.sendMessage(pluginTag + ChatColor.GREEN + "Spawned a " + args[2].toUpperCase() + " with a stack size of " + numb + " at your location.");
                        } else {
                            sender.sendMessage(pluginTag + errorTag +
                                    "Invalid entity type!");
                        }
                    } else {
                        sender.sendMessage(pluginTag + errorTag +
                                "You need to be a player to do this!");
                    }
                } else {
                    sender.sendMessage(pluginTag + errorTag +
                            "Incorrect command parameters!");
                }
            } else {
                sender.sendMessage(pluginTag + errorTag +
                        "Incorrect command parameters!");
            }
        }else{
            sender.sendMessage(noPerm);
        }
        return false;
    }
}
