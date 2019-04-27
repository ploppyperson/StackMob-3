package uk.antiperson.stackmob;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.tools.GlobalValues;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class Commands implements CommandExecutor {

    private StackMob sm;

    public Commands(StackMob sm) {
        this.sm = sm;
    }

    private final String noPerm = GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
        "You do not have the permission to perform this command! If you believe this is in error, contact the server administration.";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if (!sender.hasPermission("StackMob.*") && !sender.hasPermission("StackMob.Admin")) {
            sender.sendMessage(noPerm);
            return true;
        }

        if (args.length == 0) {
            cmdHelp(sender);
            return true;
        }

        switch (args[0]) {
            case "help":
                cmdHelp(sender);
                break;

            case "about": {
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GOLD + "StackMob v" + sm.getDescription().getVersion() + " by antiPerson and contributors.");
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.YELLOW + "Find out more at " + sm.getDescription().getWebsite());
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.YELLOW + "Find the source code at " + GlobalValues.GITHUB);
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.YELLOW + "Has this plugin helped your server? Please leave a review!");
                break;
            }

            case "reset": {
                sm.getConfigFile().getF().delete();
                sm.getConfigFile().reloadCustomConfig();
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "The configuration has been reset and reloaded.");
                break;
            }

            case "reload": {
                sm.getConfigFile().reloadCustomConfig();
                sm.getGeneralFile().reloadCustomConfig();
                sm.getTranslationFile().reloadCustomConfig();
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "The configuration files have been reloaded.");
                break;
            }

            case "removeall": {
                int counter = 0;
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getLivingEntities()) {
                        if (StackTools.hasValidData(entity)) {
                            counter++;
                            entity.remove();
                            sm.getLogic().cleanup(entity);
                        }
                    }
                }
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "A total of " + counter + " entities were removed.");
                break;
            }

            case "removetype": {
                if (!checkArgs(sender, args, 2)) break;

                // This is a little overkill for one case, but it's easily extensible for other filters.
                Function<Entity, Boolean> filter;
                switch (args[1]) {
                    case "hostile":
                        filter = entity -> entity instanceof Monster;
                        break;

                    default:
                        // If any other cases are added, they should also be added to this error.
                        sender.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
                            "Invalid filter type, expected 'hostile'");
                        return true;
                }

                int counter = 0;
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getLivingEntities()) {
                        if (filter.apply(entity) && StackTools.hasValidData(entity)) {
                            counter++;
                            entity.remove();
                            sm.getLogic().cleanup(entity);
                        }
                    }
                }
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "A total of " + counter + " entities were removed.");
                break;
            }

            case "check":
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GOLD + sm.getUpdater().updateString());
                break;

            case "update":
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GOLD + sm.getUpdater().update());
                break;

            case "stats": {
                int stackedTotal = 0;
                for (int a : StackTools.getEntries().values()) {
                    if (a > 0) {
                        stackedTotal += a;
                        continue;
                    }
                    stackedTotal += 1;
                }

                int stackedCount1 = 0;
                int stackedTotal1 = 0;
                if (sender instanceof Player) {
                    for (Entity entity : ((Player) sender).getLocation().getChunk().getEntities()) {
                        if (StackTools.hasValidStackData(entity)) {
                            stackedCount1 += 1;
                            stackedTotal1 += StackTools.getSize(entity);
                        }
                    }
                }

                int cacheTotal = 0;
                for (UUID uuid : sm.getCache().keySet()) {
                    if (sm.getCache().get(uuid) > 0) {
                        cacheTotal += sm.getCache().get(uuid);
                    }
                }

                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GOLD + "Entity stacking statistics:");
                sender.sendMessage(ChatColor.YELLOW + "Loaded entities: " + ChatColor.GREEN + StackTools.getEntries().size() + " (" + stackedTotal + " stacked.) "
                    + ChatColor.YELLOW + "Loaded entities (this chunk): " + ChatColor.GREEN + stackedCount1 + " (" + stackedTotal1 + " stacked.) ");
                sender.sendMessage(ChatColor.YELLOW + "Cached entities: " + ChatColor.GREEN + sm.getCache().size() + " (" + cacheTotal + " stacked.) ");
                break;
            }

            case "stick":
            case "tool": {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    sm.getStickTools().giveStackingStick(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
                    player.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.YELLOW + "The stacking tool has been added to your inventory.");
                } else {
                    sender.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
                        "You need to be a player to do this!");
                }
                break;
            }

            case "cleanup": {
                int counter = 0;
                Iterator<Map.Entry<UUID, Integer>> iterator = sm.getCache().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<UUID, Integer> entry = iterator.next();
                    if (entry.getValue() == GlobalValues.NOT_ENOUGH_NEAR || entry.getValue() == 1) {
                        iterator.remove();
                        counter++;
                    }
                }
                sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "Removed " + counter + " single stacks from the cache.");
                break;
            }

            case "remove": {
                if (!checkArgs(sender, args, 2) || !checkPlayer(sender)) break;

                try {
                    int numb = Integer.valueOf(args[1]);
                    int counter = 0;
                    for (Entity entity : ((Player) sender).getNearbyEntities(numb, numb, numb)) {
                        if (StackTools.hasValidData(entity)) {
                            entity.remove();
                            sm.getLogic().cleanup(entity);
                            counter++;
                        }
                    }
                    sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "A total of " + counter + " entities were removed.");
                } catch (NumberFormatException e) {
                    sender.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
                        "Invalid number format!");
                }
                break;
            }

            case "spawnstack": {
                if (!checkArgs(sender, args, 3) || !checkPlayer(sender)) break;

                int numb;
                try {
                    numb = Integer.valueOf(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
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
                    StackTools.setSize(newEntity, numb);
                    sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "Spawned a " + args[2].toUpperCase() + " with a stack size of " + numb + " at your location.");
                } else {
                    sender.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
                        "Invalid entity type!");
                }
                break;
            }

            default:
                sender.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
                    "Incorrect command parameters!");
                break;
        }
        return true;
    }

    private void cmdHelp(CommandSender sender) {
        sender.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GOLD + "Plugin commands:");
        sender.sendMessage(ChatColor.AQUA + "/sm spawnstack [size] [entity type] " + ChatColor.GREEN + "Spawns a new pre-stacked entity.");
        sender.sendMessage(ChatColor.AQUA + "/sm remove [radius] " + ChatColor.GREEN + "Removes all of the stacked entities loaded in the specified radius.");
        sender.sendMessage(ChatColor.AQUA + "/sm removetype [type] " + ChatColor.GREEN + "Removes all of the stacked entities of the given type loaded.");
        sender.sendMessage(ChatColor.AQUA + "/sm removeall " + ChatColor.GREEN + "Removes all of the stacked entities loaded.");
        sender.sendMessage(ChatColor.AQUA + "/sm cleanup " + ChatColor.GREEN + "Removes all single stacks from the cache.");
        sender.sendMessage(ChatColor.AQUA + "/sm tool " + ChatColor.GREEN + "Gives you the tool of stacking.");
        sender.sendMessage(ChatColor.AQUA + "/sm stats " + ChatColor.GREEN + "Displays entity statistics.");
        sender.sendMessage(ChatColor.AQUA + "/sm reload " + ChatColor.GREEN + "Reloads the configuration file.");
        sender.sendMessage(ChatColor.AQUA + "/sm reset " + ChatColor.GREEN + "Resets the configuration file.");
        sender.sendMessage(ChatColor.AQUA + "/sm check " + ChatColor.GREEN + "Checks for version updates.");
        sender.sendMessage(ChatColor.AQUA + "/sm update " + ChatColor.GREEN + "Downloads the latest version.");
        sender.sendMessage(ChatColor.AQUA + "/sm about " + ChatColor.GREEN + "Shows plugin information.");
    }

    private boolean checkArgs(CommandSender sender, String[] args, int req) {
        if (args.length < req) {
            sender.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
                "Incorrect command parameters!");
            return false;
        }
        return true;
    }

    private boolean checkPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        }
        sender.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG +
            "You need to be a player to do this!");
        return false;
    }

}
