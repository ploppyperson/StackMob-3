package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.services.BukkitService;

@AllArgsConstructor
public class BreedListener implements Listener {

    private BukkitService bukkitService;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBreed(EntityBreedEvent event) {
        LivingEntity father = event.getFather();
        LivingEntity mother = event.getMother();
        if (father.hasMetadata(GlobalValues.CURRENTLY_BREEDING) && father.getMetadata(GlobalValues.CURRENTLY_BREEDING).get(0).asBoolean()) {
            bukkitService.setMetadata(father, GlobalValues.CURRENTLY_BREEDING, false);
            bukkitService.setMetadata(father, GlobalValues.NO_STACK_ALL, false);
        }
        if (mother.hasMetadata(GlobalValues.CURRENTLY_BREEDING) && mother.getMetadata(GlobalValues.CURRENTLY_BREEDING).get(0).asBoolean()) {
            bukkitService.setMetadata(mother, GlobalValues.CURRENTLY_BREEDING, false);
            bukkitService.setMetadata(mother, GlobalValues.NO_STACK_ALL, false);
        }
    }

}
