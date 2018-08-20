package uk.antiperson.stackmob.listeners.player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.UUID;

public class ChatEvent implements Listener {

    private StackMob sm;
    public ChatEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if(!(GeneralTools.hasInvaildMetadata(player, GlobalValues.WAITING_FOR_INPUT))){
            if(!(player.getMetadata(GlobalValues.WAITING_FOR_INPUT).get(0).asBoolean())){
                return;
            }
            String uuid = player.getMetadata(GlobalValues.SELECTED_ENTITY).get(0).asString();
            Entity entity = Bukkit.getEntity(UUID.fromString(uuid));
            event.setCancelled(true);
            try {
                int stackSize = Integer.parseInt(event.getMessage());
                entity.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, stackSize));
            }catch (NumberFormatException e){
                player.sendMessage(GlobalValues.PLUGIN_TAG + GlobalValues.ERROR_TAG + "Invalid input!");
                player.sendMessage(GlobalValues.PLUGIN_TAG + ChatColor.GREEN + "Enter new stack value: ");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1,1);
                return;
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Updated entity stack size!"));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
            player.removeMetadata(GlobalValues.WAITING_FOR_INPUT, sm);
            player.removeMetadata(GlobalValues.SELECTED_ENTITY, sm);
        }
    }
}
