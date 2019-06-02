package uk.antiperson.stackmob.api.entity.death;

import org.bukkit.entity.LivingEntity;

public interface DeathMethod {

    int calculateStep(LivingEntity dead);

}
