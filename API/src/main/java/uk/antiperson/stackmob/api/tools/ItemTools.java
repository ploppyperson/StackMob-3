package uk.antiperson.stackmob.api.tools;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemTools {

    public static void damageItemInHand(Player player, int stackSize){
        ItemStack item = player.getInventory().getItemInMainHand();
        Damageable meta = (Damageable) item.getItemMeta();
        int newDamage = meta.getDamage() + stackSize;
        if(newDamage <= item.getType().getMaxDurability()){
            meta.setDamage(meta.getDamage() + stackSize);
            item.setItemMeta((ItemMeta) meta);
            player.getInventory().setItemInMainHand(item);
            return;
        }
        player.getInventory().setItemInMainHand(null);
    }

    public static boolean hasEnoughDurability(Player player, int stackSize) {
        ItemStack item = player.getInventory().getItemInMainHand();
        Damageable meta = (Damageable) item.getItemMeta();
        int newDamage = meta.getDamage() + stackSize;
        if(newDamage >= item.getType().getMaxDurability()){
            player.sendActionBar(ChatColor.RED + "Item is too damaged to shear all!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            return false;
        }
        return true;
    }
}
