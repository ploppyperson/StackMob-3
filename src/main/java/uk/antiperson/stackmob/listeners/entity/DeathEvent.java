package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.death.DeathMethod;
import uk.antiperson.stackmob.entity.death.DeathStep;
import uk.antiperson.stackmob.entity.death.DeathType;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.entity.death.method.KillStepDamage;
import uk.antiperson.stackmob.tools.GlobalValues;

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
        if(!(StackTools.hasValidStackData(dead))){
            return;
        }

        DeathStep method = calculateMethod(dead);
        int stackAmount = StackTools.getSize(dead);
        int subtractAmount = calculateStep(dead, method);
        multiplication(dead, e.getDrops(), subtractAmount - 1, e.getDroppedExp());
        if(subtractAmount != stackAmount){
            Entity entity = spawnNewEntity(stackAmount - subtractAmount, dead);
            if(method instanceof KillStepDamage){
                ((KillStepDamage)method).onceSpawned(dead, (LivingEntity) entity);
            }
        }
        sm.getLogic().cleanup(dead);
    }

    private void multiplication(LivingEntity dead, List<ItemStack> drops, int subtractAmount, int originalExperience){
        if(sm.getCustomConfig().getBoolean("multiply-drops.enabled")){
            sm.getDropTools().calculateDrops(drops, subtractAmount, dead);
        }
        if(sm.getCustomConfig().getBoolean("multiply-exp.enabled")){
            // double newExperience = subtractAmount * (originalExperience * sm.config.getCustomConfig().getDouble("multiply-exp-scaling", 1.0));
            ExperienceOrb exp = (ExperienceOrb) dead.getWorld().spawnEntity(dead.getLocation(), EntityType.EXPERIENCE_ORB);
            exp.setExperience(sm.getExpTools().multiplyExperience(originalExperience, subtractAmount));
        }
        if(sm.getCustomConfig().getBoolean("increase-player-stats")){
            if(dead.getKiller() != null){
                int oldStat = dead.getKiller().getStatistic(Statistic.MOB_KILLS);
                dead.getKiller().setStatistic(Statistic.MOB_KILLS, oldStat + subtractAmount);
            }
        }
    }

    private DeathStep calculateMethod(LivingEntity dead){
        if(!dead.hasMetadata(GlobalValues.KILL_ONE)){
            for(DeathType deathType : DeathType.values()){
                DeathStep method = sm.getDeathManager().getMethod(deathType);
                if(method.isAllowed(dead)){
                    return method;
                }
            }
        }
        return null;
    }

    private int calculateStep(LivingEntity dead, DeathStep method){
        if(method != null){
            return method.calculateStep(dead);
        }
        return 1;
    }

    private Entity spawnNewEntity(int newSize, LivingEntity dead){
        Entity newe = sm.getTools().duplicate(dead);
        StackTools.setSize(newe, newSize);
        return newe;
    }

}
