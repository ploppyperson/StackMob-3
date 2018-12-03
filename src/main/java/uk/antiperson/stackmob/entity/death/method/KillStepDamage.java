package uk.antiperson.stackmob.entity.death.method;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.death.DeathStep;
import uk.antiperson.stackmob.entity.death.DeathType;
import uk.antiperson.stackmob.tools.GlobalValues;

public class KillStepDamage extends DeathStep {

    public KillStepDamage(StackMob sm){
        super(sm, DeathType.KILL_STEP_DAMAGE);
    }

    @Override
    public int calculateStep(LivingEntity dead) {
        double damageDivided = getLeftoverDamage(dead) / getMaxHealth(dead);
        return (int) Math.floor(damageDivided) + 1;
    }

    public void onceSpawned(LivingEntity dead, LivingEntity spawned){
        double damageDivided = getLeftoverDamage(dead) / getMaxHealth(dead);
        double killStep = Math.floor(damageDivided);
        double damageToDeal = (damageDivided - killStep) * getMaxHealth(dead);
        spawned.setHealth(spawned.getHealth() - damageToDeal);
    }

    private double getLeftoverDamage(LivingEntity dead){
        return dead.getMetadata(GlobalValues.LEFTOVER_DAMAGE).get(0).asDouble();
    }

    private double getMaxHealth(LivingEntity dead){
        return dead.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }
}
