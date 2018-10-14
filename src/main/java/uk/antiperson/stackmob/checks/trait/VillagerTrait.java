package uk.antiperson.stackmob.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import uk.antiperson.stackmob.checks.ApplicableTrait;
import uk.antiperson.stackmob.checks.TraitChecks;

public class VillagerTrait implements ApplicableTrait {

    public VillagerTrait(TraitChecks tc){
        if(tc.getStackMob().config.getCustomConfig().getBoolean("compare.villager-profession")) {
            tc.registerTrait(this);
        }
    }

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Villager){
            if (((Villager) original).getProfession() != ((Villager) nearby).getProfession()) {
                return true;
            }
            return (((Villager) original).getCareer() != ((Villager) nearby).getCareer());
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Villager){
            ((Villager) original).setProfession(((Villager) original).getProfession());
            ((Villager) original).setCareer(((Villager) original).getCareer());
        }
    }
}
