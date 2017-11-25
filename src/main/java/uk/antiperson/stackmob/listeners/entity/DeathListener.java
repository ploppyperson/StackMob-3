package uk.antiperson.stackmob.listeners.entity;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.GlobalValues;
import uk.antiperson.stackmob.config.ConfigLoader;
import uk.antiperson.stackmob.services.BukkitService;
import uk.antiperson.stackmob.services.DropService;
import uk.antiperson.stackmob.services.EntityService;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class DeathListener implements Listener {

    private ConfigLoader config;
    private EntityService entityService;
    private DropService dropService;
    private BukkitService bukkitService;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDeathEvent event) {
        Entity deadEntity = event.getEntity();

        if (!deadEntity.hasMetadata(GlobalValues.METATAG)) {
            return;
        }
        if (deadEntity.getMetadata(GlobalValues.METATAG).get(0).asInt() <= 1) {
            return;
        }

        int oldSize = deadEntity.getMetadata(GlobalValues.METATAG).get(0).asInt();
        int subtractAmount = 1;


        if (config.get().getBoolean("kill-all.enabled")) {
            if (!config.get().getStringList("kill-all.reason-blacklist")
                    .contains(deadEntity.getLastDamageCause().getCause().toString())) {
                if (!config.get().getStringList("kill-all.type-blacklist")
                        .contains(deadEntity.getType().toString())) {
                    // Do it
                    multiplication(event.getEntity(), event.getDrops(), oldSize - 1, event.getDroppedExp());
                    if (config.get().getBoolean("multiply-exp-enabled")) {
                        event.setDroppedExp((int) Math.round((1.25 + ThreadLocalRandom.current().nextDouble(0.75)) * (oldSize - 1) * event.getDroppedExp()));
                    }
                    if (deadEntity instanceof Slime) {
                        entityService.spawnMoreSlime((Slime) deadEntity, oldSize - 1);
                    }
                    return;
                }
            }
        }

        if (config.get().getBoolean("kill-step.enabled")) {
            if (!config.get().getStringList("kill-step.reason-blacklist")
                    .contains(deadEntity.getLastDamageCause().getCause().toString())) {
                if (!config.get().getStringList("kill-step.type-blacklist")
                        .contains(deadEntity.getType().toString())) {
                    int randomStep = ThreadLocalRandom.current().nextInt(1, config.get().getInt("kill-step.max-step"));
                    if (randomStep >= oldSize) {
                        subtractAmount = oldSize;
                    } else {
                        subtractAmount = randomStep;
                    }
                    multiplication(event.getEntity(), event.getDrops(), subtractAmount - 1, event.getDroppedExp());
                    if (config.get().getBoolean("multiply-exp-enabled")) {
                        event.setDroppedExp((int) Math.round((1.45 + ThreadLocalRandom.current().nextDouble(0.75)) * (subtractAmount - 1) * event.getDroppedExp()));
                    }
                    if (deadEntity instanceof Slime) {
                        entityService.spawnMoreSlime((Slime) deadEntity, subtractAmount - 1);
                    }
                }
            }
        }

        if (oldSize != subtractAmount) {
            Entity newEntity = entityService.duplicate(deadEntity);
            bukkitService.setMetadata(newEntity, GlobalValues.METATAG, oldSize - subtractAmount);
            bukkitService.setMetadata(newEntity, GlobalValues.NO_SPAWN_STACK, true);
        }
        bukkitService.removeMetadata(deadEntity, GlobalValues.METATAG);
        bukkitService.removeMetadata(deadEntity, GlobalValues.NO_STACK_ALL);
        bukkitService.removeMetadata(deadEntity, GlobalValues.NO_TASK_STACK);
        bukkitService.removeMetadata(deadEntity, GlobalValues.CURRENTLY_BREEDING);
        bukkitService.removeMetadata(deadEntity, GlobalValues.NOT_ENOUGH_NEAR);
    }

    // TODO: originalExperience ?
    public void multiplication(LivingEntity dead, List<ItemStack> drops, int subtractAmount, int originalExperience) {
        if (config.get().getBoolean("multiply-drops.enabled")) {
            if (dead.getKiller() != null) {
                dropService.calculateDrops(drops, subtractAmount, dead.getLocation(), dead.getKiller().getItemInHand());
            } else {
                dropService.calculateDrops(drops, subtractAmount, dead.getLocation(), null);
            }
        }
    }
}
