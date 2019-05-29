package uk.antiperson.stackmob.api.entity.death;

import org.bukkit.entity.LivingEntity;

public interface IDeathManager {
    DeathStep calculateMethod(LivingEntity dead);

    int calculateStep(LivingEntity dead, DeathStep method);
}
