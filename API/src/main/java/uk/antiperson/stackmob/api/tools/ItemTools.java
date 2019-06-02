package uk.antiperson.stackmob.api.tools;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemTools {

    public static void damageItemInHand(Player player, int stackSize) {
        ItemStack is = damageItem(player.getInventory().getItemInMainHand(), stackSize);
        player.getInventory().setItemInMainHand(is);
    }

    public static ItemStack damageItem(ItemStack item, int stackSize){
        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(meta.getDamage() + stackSize);
        item.setItemMeta((ItemMeta) meta);
        return item;
    }

    public static boolean hasEnoughDurability(Player player, int stackSize) {
        if(!hasEnoughDurability(player.getInventory().getItemInMainHand(), stackSize)){
            player.sendActionBar(ChatColor.RED + "Item is too damaged to shear all!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            return false;
        }
        return true;
    }

    public static boolean hasEnoughDurability(ItemStack item, int stackSize) {
        Damageable meta = (Damageable) item.getItemMeta();
        int newDamage = meta.getDamage() + stackSize;
        return newDamage <= item.getType().getMaxDurability();
    }
}
