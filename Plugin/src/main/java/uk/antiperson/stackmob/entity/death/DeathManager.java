package uk.antiperson.stackmob.entity.death;

import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.entity.death.IDeathManager;
import uk.antiperson.stackmob.entity.death.method.KillStep;
import uk.antiperson.stackmob.entity.death.method.KillStepDamage;
import uk.antiperson.stackmob.api.entity.death.DeathStep;
import uk.antiperson.stackmob.api.entity.death.DeathType;
import uk.antiperson.stackmob.entity.death.method.KillAll;
import uk.antiperson.stackmob.api.tools.GlobalValues;

import java.util.HashMap;
import java.util.Map;

public class DeathManager implements IDeathManager {

    private Map<DeathType, DeathStep> deathMap = new HashMap<>();
    public DeathManager(IStackMob sm){
        deathMap.put(DeathType.KILL_ALL, new KillAll(sm));
        deathMap.put(DeathType.KILL_STEP, new KillStep(sm));
        deathMap.put(DeathType.KILL_STEP_DAMAGE, new KillStepDamage(sm));
    }

    private DeathStep getMethod(DeathType type){
        return deathMap.get(type);
    }

    @Override
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

    @Override
    public int calculateStep(LivingEntity dead, DeathStep method){
        if(method != null){
            return method.calculateStep(dead);
        }
        return 1;
    }
}
