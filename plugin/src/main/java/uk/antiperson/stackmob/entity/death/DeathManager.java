package uk.antiperson.stackmob.entity.death;

import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.death.method.KillAll;
import uk.antiperson.stackmob.entity.death.method.KillStep;
import uk.antiperson.stackmob.entity.death.method.KillStepDamage;
import uk.antiperson.stackmob.tools.GlobalValues;

import java.util.HashMap;
import java.util.Map;

public class DeathManager {

    private Map<DeathType, DeathStep> deathMap = new HashMap<>();
    public DeathManager(StackMob sm){
        deathMap.put(DeathType.KILL_ALL, new KillAll(sm));
        deathMap.put(DeathType.KILL_STEP, new KillStep(sm));
        deathMap.put(DeathType.KILL_STEP_DAMAGE, new KillStepDamage(sm));
    }

    private DeathStep getMethod(DeathType type){
        return deathMap.get(type);
    }

    public DeathStep calculateMethod(LivingEntity dead){
        if(!dead.hasMetadata(GlobalValues.KILL_ONE)){
            for(DeathType deathType : DeathType.values()){
                DeathStep method = getMethod(deathType);
                if(method.isAllowed(dead)){
                    return method;
                }
            }
        }
        return null;
    }

    public int calculateStep(LivingEntity dead, DeathStep method){
        if(method != null){
            return method.calculateStep(dead);
        }
        return 1;
    }
}
