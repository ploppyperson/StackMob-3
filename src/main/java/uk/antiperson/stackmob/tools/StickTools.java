package uk.antiperson.stackmob.tools;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.ArrayList;
import java.util.List;

public class StickTools {

    private StackMob sm;
    private String itemName;
    private List<String> itemLore = new ArrayList<>();
    public StickTools(StackMob sm){
        this.sm = sm;
        String name = sm.translation.getCustomConfig().getString("stack-stick.name");
        itemName = ChatColor.translateAlternateColorCodes('&', name);
        for (String s : sm.translation.getCustomConfig().getStringList("stack-stick.lore")){
            itemLore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    public void giveStackingStick(Player player){
        ItemStack stack = new ItemStack(Material.STICK, 1);
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
                    if(nearby instanceof LivingEntity && !(nearby instanceof ArmorStand || nearby instanceof Player)){
                        if(!(sm.getStackTools().hasValidStackData(nearby))){
                            sm.getStackTools().setSize(nearby, 1);
                        }
                    }
                }

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Stacked all in this chunk!"));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                break;
            case UNSTACK_ONE:
                sm.getStackTools().removeSize(entity);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Removed entity stack status!"));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                entity.setCustomNameVisible(false);
                entity.setCustomName(null);
                break;
            case UNSTACK_NEARBY:
                for(Entity nearby : entity.getLocation().getChunk().getEntities()){
                    if(nearby instanceof LivingEntity && !(nearby instanceof ArmorStand || nearby instanceof Player)){
                        if(sm.getStackTools().hasValidStackData(nearby)){
                            sm.getStackTools().removeSize(nearby);
                        }
                    }
                }

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Unstacked all in this chunk!"));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
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

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Toggled tool mode to " + ChatColor.GOLD + StickMode.getStickMode(newStickMode)));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,2);
    }


}
