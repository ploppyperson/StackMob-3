package uk.antiperson.stackmob.api.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public interface IEntityTools {
    // Compares the differences between two entities
    // firstEntity and nearby are ALWAYS the same entity type!
    boolean notMatching(Entity firstEntity, Entity nearby);

    void onceStacked(Entity entity);

    Entity duplicate(Entity original);

    void setTraits(LivingEntity entity);

    void setAi(LivingEntity entity);
}
