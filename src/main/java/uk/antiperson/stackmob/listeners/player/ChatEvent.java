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
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.StackTools;
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
        if(StackTools.hasValidMetadata(player, GlobalValues.WAITING_FOR_INPUT)){
            if(!(player.getMetadata(GlobalValues.WAITING_FOR_INPUT).get(0).asBoolean())){
                return;
            }
            event.setCancelled(true);
            sm.stickTools.updateStack(player, event.getMessage());
        }
    }
}
