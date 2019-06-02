package uk.antiperson.stackmob.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.material.Wool;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.StackedEntity;
import uk.antiperson.stackmob.api.entity.IStackLogic;
import uk.antiperson.stackmob.api.entity.StackTools;
import uk.antiperson.stackmob.api.events.EntityStackEvent;
import uk.antiperson.stackmob.api.tools.GlobalValues;
import uk.antiperson.stackmob.api.tools.ItemTools;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class StackLogic implements IStackLogic {

    private StackMob sm;
    private double xRadius;
    private double yRadius;
    private double zRadius;
    public StackLogic(StackMob sm){
        this.sm = sm;
        xRadius = sm.getCustomConfig().getDouble("check-area.x");
        yRadius = sm.getCustomConfig().getDouble("check-area.y");
        zRadius = sm.getCustomConfig().getDouble("check-area.z");
    }

    @Override
    public boolean foundMatch(Entity original){
        for(Entity nearby : original.getNearbyEntities(getX(), getY(), getZ())){
            if(original.getType() != nearby.getType()){
                continue;
            }
            if(notSuitableForStacking(nearby)){
                continue;
            }
            if(sm.getTools().notMatching(original, nearby)) {
                continue;
            }
            if(callEvent(original, nearby)){
                continue;
            }
            merge(original, nearby);
            return true;
        }
        return false;
    }

    @Override
    public void merge(Entity original, Entity nearby){
        int maxSize = getMaxSize(original);
        int nearbySize = StackTools.getSize(nearby);
        int originalSize = StackTools.getSize(original);
        if(nearbySize > originalSize){
            Entity holder = nearby;
            nearby = original;
            original = holder;
        }

        // Continue to stack together
        int amountTotal = nearbySize + originalSize;
        if(amountTotal > maxSize){
            StackTools.setSize(original, maxSize);
            StackTools.setSize(nearby,amountTotal - maxSize);
        }else{
            StackTools.setSize(original, amountTotal);
            sm.getTools().onceStacked(nearby);
            nearby.remove();
        }
    }

    @Override
    public boolean notEnoughNearby(Entity original){
        int dontStackTill = sm.getCustomConfig().getInt("dont-stack-until");
        if(dontStackTill <= 1){
            return false;
        }
        List<Entity> nearbyEntities = original.getNearbyEntities(getX(), getY(), getZ());
        if(nearbyEntities.size() < dontStackTill &&
                nearbyEntities.stream().noneMatch(StackTools::hasValidStackData)){
            return true;
        }
        HashSet<Entity> entities = new HashSet<>();
        for(Entity nearby : nearbyEntities){
            if(original.getType() != nearby.getType()){
                continue;
            }
            if(!(StackTools.hasValidData(nearby))){
                continue;
            }
            if(sm.getTools().notMatching(original, nearby)){
                continue;
            }
            if(StackTools.hasValidStackData(nearby)){
                StackTools.setSize(original, 1);
                return false;
            }
            entities.add(nearby);
        }
        if(entities.size() + 1 < dontStackTill){
            return true;
        }
        for(Entity entity : entities){
            StackTools.setSize(entity,1);
        }
        return false;
    }

    @Override
    public boolean incrementWaiting(Entity entity){
        if(!StackTools.isWaiting(entity)){
            return false;
        }
        StackTools.incrementWaiting(entity);
        if(!(StackTools.isWaiting(entity))){
            StackTools.setSize(entity, GlobalValues.NOT_ENOUGH_NEAR);
            return false;
        }
        return true;
    }

    @Override
    public boolean makeWaiting(Entity entity, CreatureSpawnEvent.SpawnReason reason){
        if(!sm.getCustomConfig().getBoolean("wait-to-stack.enabled")){
            return false;
        }
        if(!sm.getCustomConfig().getStringList("wait-to-stack.entity-types")
                .contains(entity.getType().toString())){
            return false;
        }
        if(!sm.getCustomConfig().getStringList("wait-to-stack.spawn-reasons")
                .contains(reason.toString())){
            return false;
        }
        int waitingTime = sm.getCustomConfig().getInt("wait-to-stack.wait-time");
        StackTools.addWaiting(entity, waitingTime);
        return true;
    }

    @Override
    public boolean notSuitableForStacking(Entity entity){
        if(!(StackTools.hasValidStackData(entity))){
            return true;
        }
        if(entity.isDead()){
            return true;
        }
        if(StackTools.hasValidMetadata(entity, GlobalValues.NO_STACK) &&
                entity.getMetadata(GlobalValues.NO_STACK).get(0).asBoolean()){
            return true;
        }
        int stackSize = StackTools.getSize(entity);
        return (getMaxSize(entity) == stackSize);
    }

    @Override
    public int getMaxSize(Entity entity){
        int maxStackSize = sm.getCustomConfig().getInt("stack-max");
        if (sm.getCustomConfig().isInt("custom." + entity.getType() + ".stack-max")) {
            maxStackSize =  sm.getCustomConfig().getInt("custom." + entity.getType() + ".stack-max");
        }
        return maxStackSize;
    }

    @Override
    public boolean doSpawnChecks(Entity entity, String reason){
        if(sm.getConfigFile().check("stack-reasons", reason)){
            StackTools.setSize(entity, GlobalValues.NO_STACKING);
            return true;
        }
        return doChecks(entity);
    }

    @Override
    public boolean doChecks(Entity entity){
        if(sm.getConfigFile().check("stack-types", entity.getType().toString())){
            return true;
        }
        return sm.getConfigFile().check("stack-worlds", entity.getWorld().getName());
    }

    @Override
    public Entity duplicate(Entity original, int duplicateSize){
        return  null;
    }

    private double getX(){
        return xRadius;
    }

    private double getY() {
        return yRadius;
    }

    private double getZ() {
        return zRadius;
    }

    @Override
    public boolean callEvent(Entity original, Entity nearby){
        StackedEntity entity1 = new StackedEntity(original, sm);
        StackedEntity entity2 = new StackedEntity(nearby, sm);
        EntityStackEvent event = new EntityStackEvent(entity1, entity2);
        sm.getServer().getPluginManager().callEvent(event);
        return event.isCancelled();
    }

    @Override
    public void cleanup(Entity dead){
        dead.removeMetadata(GlobalValues.NO_STACK, sm);
        dead.removeMetadata(GlobalValues.KILL_ONE, sm);
        dead.removeMetadata(GlobalValues.LEFTOVER_DAMAGE, sm);
        StackTools.removeSize(dead);
    }

    @Override
    public boolean doSheepShearAll(Sheep sheared, ItemStack tool) {
        int stackSize = StackTools.getSize(sheared);
        if(sm.getCustomConfig().getBoolean("multiply.sheep-wool") && ItemTools.hasEnoughDurability(tool, stackSize)){
            LootContext lootContext = new LootContext.Builder(sheared.getLocation()).lootedEntity(sheared).build();
            Collection<ItemStack> loot = sheared.getLootTable().populateLoot(ThreadLocalRandom.current(), lootContext);
            for(ItemStack itemStack : loot){
                if(itemStack.getData() instanceof Wool) {
                    sm.getDropTools().dropDrops(itemStack, sm.getDropTools().calculateAmount(stackSize), sheared.getLocation());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void doSheepShearSingle(Sheep sheared) {
        int stackSize = StackTools.getSize(sheared);
        if(sm.getCustomConfig().getBoolean("divide-on.sheep-shear")){
            Sheep newEntity = (Sheep) sm.getTools().duplicate(sheared);
            newEntity.setSheared(false);

            StackTools.setSize(newEntity,stackSize - 1);
            StackTools.makeSingle(sheared);
        }
    }
}
