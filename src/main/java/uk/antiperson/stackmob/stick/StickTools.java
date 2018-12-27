package uk.antiperson.stackmob.stick;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.tools.GlobalValues;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StickTools {

    private StackMob sm;
    private String itemName;
    private List<String> itemLore = new ArrayList<>();
    private Material material;
    public StickTools(StackMob sm){
        this.sm = sm;
        String name = sm.getGeneralConfig().getString("stack-tool.name");
        itemName = ChatColor.translateAlternateColorCodes('&', name);
        for (String s : sm.getGeneralConfig().getStringList("stack-tool.lore")){
            itemLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        material = Material.matchMaterial(sm.getCustomConfig().getString("stack-tool.material"));
    }

    public void giveStackingStick(Player player){
        ItemStack stack = new ItemStack(material, 1);
        stack.addUnsafeEnchantment(Enchantment.DURABILITY,1);

        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(itemName);
        stackMeta.setLore(itemLore);
        stack.setItemMeta(stackMeta);

        player.getInventory().addItem(stack);
    }

    public boolean isStackingStick(ItemStack stack){
        return stack.getItemMeta() != null && stack.getItemMeta().getDisplayName().equals(itemName);
    }

    public void performAction(Player player, Entity entity){
        int stickMode = player.getMetadata(GlobalValues.STICK_MODE).get(0).asInt();
        switch (StickMode.getStickMode(stickMode)){
            case STACK_ONE:
                player.setMetadata(GlobalValues.WAITING_FOR_INPUT, new FixedMetadataValue(sm, true));
                player.setMetadata(GlobalValues.SELECTED_ENTITY, new FixedMetadataValue(sm, entity.getUniqueId().toString()));
                player.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "Enter new stack value: ");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1,1);
                break;
            case STACK_NEARBY:
                for(Entity nearby : entity.getLocation().getChunk().getEntities()){
                    if(nearby instanceof Mob){
                        if(!(StackTools.hasValidStackData(nearby))){
                            StackTools.setSize(nearby, 1);
                        }
                    }
                }
                sendMessage(player,ChatColor.GREEN + "Stacked all in this chunk!", 1);
                break;
            case UNSTACK_ONE:
                StackTools.removeSize(entity);
                sendMessage(player,ChatColor.GREEN + "Removed entity stack status!", 1);
                break;
            case UNSTACK_NEARBY:
                for(Entity nearby : entity.getLocation().getChunk().getEntities()){
                    if(nearby instanceof Mob){
                        if(StackTools.hasValidStackData(nearby)){
                            StackTools.removeSize(nearby);
                        }
                    }
                }
                sendMessage(player,ChatColor.GREEN + "Unstacked all in this chunk!", 1);
                break;
            case SPLIT_ONE:
                if(StackTools.hasSizeMoreThanOne(entity)){
                    Entity duplicate = sm.getTools().duplicate(entity);
                    int stackSize = StackTools.getSize(entity);
                    StackTools.setSize(duplicate, stackSize - 1);
                    StackTools.removeSize(entity);
                    sendMessage(player, ChatColor.GREEN + "Split entity from this stack!", 1);
                    break;
                }
                sendMessage(player, ChatColor.RED + "Entity stack size must be more than one!", 3);
                break;
            case DATA:
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1, 1);
                player.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GOLD + "Entity data: ");
                player.sendMessage(ChatColor.GREEN + "UUID: " + entity.getUniqueId());
                if(StackTools.hasValidData(entity)){
                    player.sendMessage(ChatColor.GREEN + "Stack size: " + StackTools.getSize(entity));
                }
                if(StackTools.isWaiting(entity)){
                    player.sendMessage(ChatColor.GREEN + "Waiting to stack: " + StackTools.isWaiting(entity) + " (" + StackTools.getWaitingTime(entity) + ")");
                }
                break;
        }
    }

    public void toggleMode(Player player){
        int newStickMode = 1;
        if(StackTools.hasValidMetadata(player, GlobalValues.STICK_MODE)){
            int stickMode = player.getMetadata(GlobalValues.STICK_MODE).get(0).asInt();
            if(stickMode != StickMode.values().length){
                newStickMode = stickMode + 1;
            }
        }
        player.setMetadata(GlobalValues.STICK_MODE, new FixedMetadataValue(sm, newStickMode));
        sendMessage(player,ChatColor.GREEN + "Toggled tool mode to " + ChatColor.GOLD + StickMode.getStickMode(newStickMode), 2);
    }

    public void updateStack(Player player, String input){
        String uuid = player.getMetadata(GlobalValues.SELECTED_ENTITY).get(0).asString();
        Entity entity = Bukkit.getEntity(UUID.fromString(uuid));
        try {
            int stackSize = Integer.parseInt(input);
            StackTools.setSize(entity, stackSize);
        }catch (NumberFormatException e){
            player.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG + "Invalid input!");
            player.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "Enter new stack value: ");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1,1);
            return;
        }
        sendMessage(player,ChatColor.GREEN + "Updated entity stack size!", 1);
        player.removeMetadata(GlobalValues.WAITING_FOR_INPUT, sm);
        player.removeMetadata(GlobalValues.SELECTED_ENTITY, sm);
    }

    public void sendMessage(Player player, String message, int pitch){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1, pitch);
    }

}
