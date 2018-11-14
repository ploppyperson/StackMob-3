package uk.antiperson.stackmob.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

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
