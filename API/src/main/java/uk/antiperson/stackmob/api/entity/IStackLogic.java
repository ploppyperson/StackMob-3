package uk.antiperson.stackmob.api.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

public interface IStackLogic {
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

    boolean doSheepShearAll(Sheep sheared, ItemStack tool);

    void doSheepShearSingle(Sheep sheared);
}
