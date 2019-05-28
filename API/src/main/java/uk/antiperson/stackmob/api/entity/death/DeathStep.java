package uk.antiperson.stackmob.api.entity.death;

import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.api.StackMob;

public abstract class DeathStep implements DeathMethod {

    private DeathType dt;
    private StackMob sm;
    public DeathStep(StackMob sm, DeathType dt){
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

    public StackMob getStackMob() {
        return sm;
    }
}
