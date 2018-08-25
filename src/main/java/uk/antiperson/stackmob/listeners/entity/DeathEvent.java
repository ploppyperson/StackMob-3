package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.tools.GeneralTools;
import uk.antiperson.stackmob.tools.extras.GlobalValues;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DeathEvent implements Listener {

    private StackMob sm;

    public DeathEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDeathEvent e) {
        LivingEntity dead = e.getEntity();
        if(GeneralTools.hasInvaildMetadata(dead)){
            return;
        }

        int oldSize = dead.getMetadata(GlobalValues.METATAG).get(0).asInt();
        int subtractAmount = 1;

        if(!dead.hasMetadata(GlobalValues.KILL_ONE_OFF)){
            if(sm.config.getCustomConfig().getBoolean("kill-all.enabled")){
               if(isKillAllAllowed(dead, dead.getKiller())){
                   multiplication(dead, e.getDrops(), oldSize - 1, e.getDroppedExp());
                   finished(oldSize, oldSize, dead);
                   return;
                }
            }

            if(sm.config.getCustomConfig().getBoolean("kill-step.enabled")){
                if(isKillStepAllowed(dead, dead.getKiller())) {
                    int randomStep = ThreadLocalRandom.current().nextInt(1, sm.config.getCustomConfig().getInt("kill-step.max-step"));
                    if (randomStep >= oldSize) {
                        subtractAmount = oldSize;
                    } else {
                        subtractAmount = randomStep;
                    }
                    multiplication(dead, e.getDrops(), subtractAmount - 1, e.getDroppedExp());
                }
            }
        }
        finished(oldSize, subtractAmount, dead);
    }

    private void multiplication(LivingEntity dead, List<ItemStack> drops, int subtractAmount, int originalExperience){
        if(sm.config.getCustomConfig().getBoolean("multiply-drops.enabled")){
            if(dead.getKiller() != null){
                sm.dropTools.calculateDrops(drops, subtractAmount, dead, dead.getKiller().getInventory().getItemInMainHand());
            }else{
                sm.dropTools.calculateDrops(drops, subtractAmount, dead, null);
            }
        }
        if(sm.config.getCustomConfig().getBoolean("multiply-exp.enabled")){
            // double newExperience = subtractAmount * (originalExperience * sm.config.getCustomConfig().getDouble("multiply-exp-scaling", 1.0));
            ExperienceOrb exp = (ExperienceOrb) dead.getWorld().spawnEntity(dead.getLocation(), EntityType.EXPERIENCE_ORB);
            exp.setExperience(sm.expTools.multiplyExperience(originalExperience, subtractAmount));
        }
    }

    private void finished(int oldSize, int subtractAmount, Entity dead){
        if(oldSize != subtractAmount){
            Entity newe = sm.tools.duplicate(dead);
            newe.setMetadata(GlobalValues.METATAG, new FixedMetadataValue(sm, oldSize - subtractAmount));
            newe.setMetadata(GlobalValues.NO_SPAWN_STACK, new FixedMetadataValue(sm, true));
        }
        dead.removeMetadata(GlobalValues.METATAG, sm);
        dead.removeMetadata(GlobalValues.NO_STACK_ALL, sm);
        dead.removeMetadata(GlobalValues.NO_TASK_STACK, sm);
        dead.removeMetadata(GlobalValues.CURRENTLY_BREEDING, sm);
        dead.removeMetadata(GlobalValues.NOT_ENOUGH_NEAR, sm);
        dead.removeMetadata(GlobalValues.KILL_ONE_OFF, sm);
    }

    private boolean isKillAllAllowed(Entity dead, Player killer){
        if(sm.config.getCustomConfig().getBoolean("death-type-permission")){
            if(killer != null){
                if(!(killer.hasPermission("stackmob.killall"))){
                    return false;
                }
            }
        }
        if (!(sm.config.getCustomConfig().getStringList("kill-all.reason-blacklist")
                .contains(dead.getLastDamageCause().getCause().toString()))){
            return !(sm.config.getCustomConfig().getStringList("kill-all.type-blacklist")
                    .contains(dead.getType().toString()));
        }
        return false;
    }

    private boolean isKillStepAllowed(Entity dead, Player killer){
        if(sm.config.getCustomConfig().getBoolean("death-type-permission")){
            if(killer != null){
                if(!(killer.hasPermission("stackmob.killstep"))){
                    return false;
                }
            }
        }
        if (!sm.config.getCustomConfig().getStringList("kill-step.reason-blacklist")
                .contains(dead.getLastDamageCause().getCause().toString())){
            return !sm.config.getCustomConfig().getStringList("kill-step.type-blacklist")
                    .contains(dead.getType().toString());
        }
        return false;
    }
}
