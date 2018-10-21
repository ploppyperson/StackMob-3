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
                    sm.stickTools.toggleMode(player);
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
