package uk.antiperson.stackmob.death;

import org.bukkit.entity.LivingEntity;

public interface DeathMethod {

    int calculateStep(LivingEntity dead);

}
