package uk.antiperson.stackmob.death;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.death.method.KillAll;
import uk.antiperson.stackmob.death.method.KillStep;
import uk.antiperson.stackmob.death.method.KillStepDamage;

import java.util.HashMap;

public class DeathManager {

    private HashMap<DeathType, DeathStep> deathMap = new HashMap<>();
    public DeathManager(StackMob sm){
        deathMap.put(DeathType.KILL_ALL, new KillAll(sm));
        deathMap.put(DeathType.KILL_STEP, new KillStep(sm));
        deathMap.put(DeathType.KILL_STEP_DAMAGE, new KillStepDamage(sm));
    }

    public DeathStep getMethod(DeathType type){
        return deathMap.get(type);
    }
}
