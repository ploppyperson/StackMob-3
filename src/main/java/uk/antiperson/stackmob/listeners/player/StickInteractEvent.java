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
                                    if(!(GeneralTools.hasValidStackData(nearby))){
                                        nearby.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, 1));
                                    }
                                }
                            }

                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Stacked all in this chunk!"));
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                            break;
                        case UNSTACK_ONE:
                            entity.removeMetadata(GlobalValues.METATAG, sm);
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Removed entity stack status!"));
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                            entity.setCustomNameVisible(false);
                            entity.setCustomName(null);
                            break;
                        case UNSTACK_NEARBY:
                            for(Entity nearby : entity.getLocation().getChunk().getEntities()){
                                if(nearby instanceof LivingEntity && !(nearby instanceof ArmorStand || nearby instanceof Player)){
                                    if(GeneralTools.hasValidStackData(nearby)){
                                        nearby.removeMetadata(GlobalValues.METATAG, sm);
                                    }
                                }
                            }

                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Unstacked all in this chunk!"));
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                            break;
                    }
                }
            }
        }
    }
}
