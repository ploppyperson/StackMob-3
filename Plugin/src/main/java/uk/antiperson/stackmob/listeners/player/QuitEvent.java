package uk.antiperson.stackmob.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.antiperson.stackmob.StackMobPlugin;
import uk.antiperson.stackmob.api.tools.GlobalValues;

public class QuitEvent implements Listener {

    private StackMobPlugin sm;
    public QuitEvent(StackMobPlugin sm){
        this.sm = sm;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(player.hasMetadata(GlobalValues.STICK_MODE)){
            player.removeMetadata(GlobalValues.STICK_MODE, sm);
            player.removeMetadata(GlobalValues.SELECTED_ENTITY, sm);
            player.removeMetadata(GlobalValues.WAITING_FOR_INPUT, sm);
        }
    }
}
