package uk.antiperson.stackmob.api.entity.death;

import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.api.IStackMob;

public abstract class DeathStep implements DeathMethod {

    private DeathType dt;
    private IStackMob sm;
    public DeathStep(IStackMob sm, DeathType dt){
        this.sm = sm;
        this.dt = dt;
    }

    public boolean isAllowed(LivingEntity dead){
        String type = dt.getType();
        if(!sm.getCustomConfig().getBoolean(type + ".enabled")){
            return false;
        }
        if(sm.getCustomConfig().getBoolean("death-type-permission")){
            if(dead.getKiller() != null){
                if(!(dead.getKiller().hasPermission("stackmob." + type))){
                    return false;
                }
            }
        }
        if (sm.getCustomConfig().getStringList(type + ".reason-blacklist")
                .contains(dead.getLastDamageCause().getCause().toString())){
            return false;
        }
        return !(sm.getCustomConfig().getStringList(type + ".type-blacklist")
                .contains(dead.getType().toString()));
    }

    public IStackMob getStackMob() {
        return sm;
    }
}
