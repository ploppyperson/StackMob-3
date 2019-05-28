package uk.antiperson.stackmob.api.entity.death;

import org.bukkit.entity.LivingEntity;

public interface DeathManager {
    DeathStep calculateMethod(LivingEntity dead);

    int calculateStep(LivingEntity dead, DeathStep method);
}
