package uk.antiperson.stackmob.api.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public interface StackLogic {
    boolean foundMatch(Entity original);

    void merge(Entity original, Entity nearby);

    boolean notEnoughNearby(Entity original);

    boolean incrementWaiting(Entity entity);

    boolean makeWaiting(Entity entity, CreatureSpawnEvent.SpawnReason reason);

    boolean notSuitableForStacking(Entity entity);

    int getMaxSize(Entity entity);

    boolean doSpawnChecks(Entity entity, String reason);

    boolean doChecks(Entity entity);

    Entity duplicate(Entity original, int duplicateSize);

    boolean callEvent(Entity original, Entity nearby);

    void cleanup(Entity dead);
}
