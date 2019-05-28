package uk.antiperson.stackmob.entity.death.method;

import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.api.StackMob;
import uk.antiperson.stackmob.api.entity.StackTools;
import uk.antiperson.stackmob.api.entity.death.DeathStep;
import uk.antiperson.stackmob.api.entity.death.DeathType;

public class KillAll extends DeathStep {

    public KillAll(StackMob sm){
        super(sm, DeathType.KILL_ALL);
    }

    @Override
    public int calculateStep(LivingEntity dead){
        return StackTools.getSize(dead);
    }
}
