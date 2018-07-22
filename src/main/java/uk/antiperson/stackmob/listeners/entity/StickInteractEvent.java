package uk.antiperson.stackmob.listeners.entity;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class StickInteractEvent implements Listener {

    private StackMob sm;
    public StickInteractEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onStickInteract(PlayerInteractEntityEvent event){
        Player p = event.getPlayer();
        if(event.getHand() == EquipmentSlot.HAND) {
            if (sm.stickTools.isStackingStick(p.getInventory().getItemInMainHand())) {
                if (p.isSneaking()) {
                    Entity entity = event.getRightClicked();
                    entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Stacked right-clicked entity."));
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                } else {
                    for (Entity entity : p.getNearbyEntities(15, 15, 15)) {
                        if(!(entity instanceof LivingEntity && entity instanceof Player && entity instanceof ArmorStand)){
                            entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                        }
                    }
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Stacked nearby entities."));
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                }
            }
        }
    }
}
