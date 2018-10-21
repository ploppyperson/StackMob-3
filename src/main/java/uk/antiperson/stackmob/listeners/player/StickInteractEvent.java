package uk.antiperson.stackmob.listeners.player;

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
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.StickMode;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class StickInteractEvent implements Listener {

    private StackMob sm;
    public StickInteractEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onStickInteract(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if(event.getHand() == EquipmentSlot.HAND) {
            if (sm.stickTools.isStackingStick(player.getInventory().getItemInMainHand())) {
                if (player.isSneaking()) {
                    int newStickMode = 1;
                    if(GeneralTools.hasValidMetadata(player, GlobalValues.STICK_MODE)){
                        int stickMode = player.getMetadata(GlobalValues.STICK_MODE).get(0).asInt();
                        if(stickMode != StickMode.values().length){
                            newStickMode = stickMode + 1;
                        }
                    }
                    player.setMetadata(GlobalValues.STICK_MODE, new FixedMetadataValue(sm, newStickMode));

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Toggled tool mode to " + ChatColor.GOLD + StickMode.getStickMode(newStickMode)));
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,2);
                    //sm.getLogger().info(entity.getUniqueId().toString() + ", " + entity.getMetadata(GlobalValues.METATAG).get(0).asInt());
                } else {
                    if(!(GeneralTools.hasValidMetadata(player, GlobalValues.STICK_MODE))){
                        player.setMetadata(GlobalValues.STICK_MODE, new FixedMetadataValue(sm, 1));
                    }
                    sm.stickTools.performAction(player, entity);
                }
            }
        }
    }
}
