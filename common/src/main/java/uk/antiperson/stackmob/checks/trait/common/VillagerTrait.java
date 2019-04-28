package uk.antiperson.stackmob.checks.trait.common;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class VillagerTrait implements ApplicableTrait {

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

    @Override
    public String getConfigPath() {
        return "compare.villager-profession";
    }
}
