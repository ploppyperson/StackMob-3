package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

public class ReceivedDamageEvent implements Listener {

    private StackMob sm;

    public ReceivedDamageEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onDamageReceived(EntityDamageEvent event) {
        if(event.getEntity() instanceof LivingEntity){
            if(!(GeneralTools.hasInvalidMetadata(event.getEntity()))){
                LivingEntity entity = (LivingEntity) event.getEntity();
                if(!sm.config.getCustomConfig().getStringList("multiply-damage-received.cause-blacklist").contains(event.getCause().toString())) {
                    int stackSize = entity.getMetadata(GlobalValues.METATAG).get(0).asInt();
                    double extraDamage = event.getDamage() + ((event.getDamage() * (stackSize - 1)) * 0.25);
                    event.setDamage(extraDamage);
                }

                if(sm.config.getCustomConfig().getBoolean("kill-step-damage.enabled")){
                    double healthAfter = entity.getHealth() - event.getFinalDamage();
                    if(healthAfter <= 0){
                        entity.setMetadata(GlobalValues.LEFTOVER_DAMAGE, new FixedMetadataValue(sm, Math.abs(healthAfter)));
                    }
                }
            }
        }
    }
}
