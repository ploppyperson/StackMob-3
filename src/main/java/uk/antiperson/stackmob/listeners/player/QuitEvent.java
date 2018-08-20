package uk.antiperson.stackmob.listeners.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class QuitEvent implements Listener {

    private StackMob sm;
    public QuitEvent(StackMob sm){
        this.sm = sm;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(event.getPlayer().hasMetadata(GlobalValues.STICK_MODE)){
            event.getPlayer().removeMetadata(GlobalValues.STICK_MODE, sm);
            event.getPlayer().removeMetadata(GlobalValues.SELECTED_ENTITY, sm);
            event.getPlayer().removeMetadata(GlobalValues.WAITING_FOR_INPUT, sm);
        }
    }
}
