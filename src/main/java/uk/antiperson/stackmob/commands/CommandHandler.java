package uk.antiperson.stackmob.commands;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.Config;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.UpdateService;
import uk.antiperson.stackmob.storage.Cache;

@AllArgsConstructor
//TODO: use the EntityManager!
public class CommandHandler implements CommandExecutor {

    private final static String PLUGIN_TAG = ChatColor.LIGHT_PURPLE + "StackMob " + ChatColor.GRAY + ">> ";
    private final static String NO_PERM = PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
            "You do not have the permission to perform this command! If you believe this is in error, contact the server administration.";

    private PluginDescriptionFile pluginDescription;
    private Config config;
    private UpdateService updateService;
    private Cache cache;
    private BukkitService bukkitService;

    @Override
    // The nest of doom.
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("StackMob.*") || sender.hasPermission("StackMob.Admin")) {
                sender.sendMessage(PLUGIN_TAG + ChatColor.GOLD + "Plugin commands:");
                sender.sendMessage(ChatColor.AQUA + "/plugin spawnstack [size] [entity type] " + ChatColor.GREEN + "Spawns a new pre-stacked entity.");
                sender.sendMessage(ChatColor.AQUA + "/plugin removeall " + ChatColor.GREEN + "Removes all of the stacked entities loaded.");
                sender.sendMessage(ChatColor.AQUA + "/plugin remove [radius] " + ChatColor.GREEN + "Removes all of the stacked entities loaded in the radius.");
                sender.sendMessage(ChatColor.AQUA + "/plugin stats " + ChatColor.GREEN + "Displays entity statistics.");
                sender.sendMessage(ChatColor.AQUA + "/plugin reload " + ChatColor.GREEN + "Reloads the configuration file.");
                sender.sendMessage(ChatColor.AQUA + "/plugin reset " + ChatColor.GREEN + "Resets the configuration file.");
                sender.sendMessage(ChatColor.AQUA + "/plugin check " + ChatColor.GREEN + "Checks for version updates.");
                sender.sendMessage(ChatColor.AQUA + "/plugin update " + ChatColor.GREEN + "Downloads the latest version.");
                sender.sendMessage(ChatColor.AQUA + "/plugin about " + ChatColor.GREEN + "Shows plugin information.");
            } else {
                sender.sendMessage(NO_PERM);
            }
        } else if (args.length == 1) {
            if (sender.hasPermission("StackMob.*") || sender.hasPermission("StackMob.Admin")) {
                if (args[0].equalsIgnoreCase("about")) {
                    sender.sendMessage(PLUGIN_TAG + ChatColor.GOLD + "StackMob v" + pluginDescription.getVersion() + " by antiPerson and contributors.");
                    sender.sendMessage(PLUGIN_TAG + ChatColor.YELLOW + "Find out more at " + pluginDescription.getWebsite());
                    sender.sendMessage(PLUGIN_TAG + ChatColor.YELLOW + "Source code can be found at " + GlobalValues.GITHUB_URL);
                    sender.sendMessage(PLUGIN_TAG + ChatColor.YELLOW + "Has this plugin helped your server? Please leave a review!");
                } else if (args[0].equalsIgnoreCase("reset")) {
                    config.reset();
                    sender.sendMessage(PLUGIN_TAG + ChatColor.GREEN + "The configuration has been reset and reloaded.");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    config.reload();
                    sender.sendMessage(PLUGIN_TAG + ChatColor.GREEN + "The configuration has been reloaded.");
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
                    sender.sendMessage(PLUGIN_TAG + ChatColor.GREEN + "A total of " + counter + " entities were removed.");
                } else if (args[0].equalsIgnoreCase("check")) {
                    updateService.checkUpdate().thenAccept(message ->
                            sender.sendMessage(PLUGIN_TAG + ChatColor.GOLD + message));
                } else if (args[0].equalsIgnoreCase("update")) {
                    updateService.update().thenAccept(message ->
                            sender.sendMessage(PLUGIN_TAG + ChatColor.GOLD + message));
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

                    sender.sendMessage(PLUGIN_TAG + ChatColor.GOLD + "Entity stacking statistics:");
                    sender.sendMessage(ChatColor.YELLOW + "Loaded entities: " + ChatColor.GREEN + stackedCount + " (" + stackedTotal + " stacked.) "
                            + ChatColor.YELLOW + "Loaded entities (this chunk): " + ChatColor.GREEN + stackedCount1 + " (" + stackedTotal1 + " stacked.) ");
                    sender.sendMessage(ChatColor.YELLOW + "Cached entities: " + ChatColor.GREEN + cache.getEntitiesCount() + " (" + cache.getTotalEntitiesCount() + " stacked.) ");
                } else {
                    sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                            "Incorrect command parameters!");
                }
            } else {
                if (args[0].equalsIgnoreCase("nick")) {
                    sender.sendMessage("england is my city");
                } else {
                    sender.sendMessage(NO_PERM);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {
                if (sender.hasPermission("StackMob.*") || sender.hasPermission("StackMob.Admin")) {
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
                            sender.sendMessage(PLUGIN_TAG + ChatColor.GREEN + "A total of " + counter + " entities were removed.");
                        } else {
                            sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                                    "Invalid number format!");
                        }
                    } else {
                        sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                                "You need to be a player to do this!");
                    }
                } else {
                    sender.sendMessage(NO_PERM);
                }
            } else {
                sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                        "Incorrect command parameters!");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("spawnstack")) {
                if (sender.hasPermission("StackMob.*") || sender.hasPermission("StackMob.Admin")) {
                    if (sender instanceof Player) {
                        Integer numb;
                        try {
                            numb = Integer.valueOf(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
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
                            bukkitService.setMetadata(newEntity, GlobalValues.NO_SPAWN_STACK, true);
                            bukkitService.setMetadata(newEntity, GlobalValues.METATAG, numb);
                            sender.sendMessage(PLUGIN_TAG + ChatColor.GREEN + "Spawned a " + args[2].toUpperCase() + " with a stack size of " + numb + " at your location.");
                        } else {
                            sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                                    "Invalid entity type!");
                        }
                    } else {
                        sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                                "You need to be a player to do this!");
                    }
                } else {
                    sender.sendMessage(NO_PERM);
                }
            } else {
                sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                        "Incorrect command parameters!");
            }
        } else {
            sender.sendMessage(PLUGIN_TAG + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED +
                    "Incorrect command parameters!");
        }
        return false;
    }
}
