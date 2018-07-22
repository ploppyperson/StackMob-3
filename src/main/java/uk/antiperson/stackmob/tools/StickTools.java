package uk.antiperson.stackmob.tools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.antiperson.stackmob.StackMob;

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


}
