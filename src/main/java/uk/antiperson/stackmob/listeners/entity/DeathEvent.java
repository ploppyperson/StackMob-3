package uk.antiperson.stackmob.listeners.entity;

import org.bukkit.Statistic;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.StackedEntity;
import uk.antiperson.stackmob.api.events.StackDeathEvent;
import uk.antiperson.stackmob.entity.death.DeathStep;
import uk.antiperson.stackmob.entity.StackTools;
import uk.antiperson.stackmob.entity.death.method.KillStepDamage;

import java.util.List;

public class DeathEvent implements Listener {

    private StackMob sm;

    public DeathEvent(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDeathEvent e) {
        LivingEntity dead = e.getEntity();
        if(!(dead instanceof Mob)){
            return;
        }
        if(!(StackTools.hasValidStackData(dead))){
            return;
        }
        int stackAmount = StackTools.getSize(dead);
        
        DeathStep method = sm.getDeathManager().calculateMethod(dead);
        int subtractAmount = sm.getDeathManager().calculateStep(dead, method);
        callEvent(dead, e, subtractAmount);
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
            sm.getDropTools().doDrops(subtractAmount, dead);
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

    private Entity spawnNewEntity(int newSize, LivingEntity dead){
        Entity newe = sm.getTools().duplicate(dead);
        StackTools.setSize(newe, newSize);
        return newe;
    }

    private void callEvent(Entity entity, EntityDeathEvent event, int deathAmount){
        StackedEntity stackedEntity = new StackedEntity(entity, sm);
        StackDeathEvent sde = new StackDeathEvent(stackedEntity, event, deathAmount);
        sm.getServer().getPluginManager().callEvent(sde);
    }
}
