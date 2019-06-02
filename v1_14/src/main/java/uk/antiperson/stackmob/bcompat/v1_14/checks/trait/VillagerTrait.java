package uk.antiperson.stackmob.bcompat.v1_14.checks.trait;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;

public class VillagerTrait implements ApplicableTrait {

    @Override
    public boolean checkTrait(Entity original, Entity nearby) {
        if(original instanceof Villager){
            return ((Villager) original).getProfession() != ((Villager) nearby).getProfession();
        }
        return false;
    }

    @Override
    public void applyTrait(Entity original, Entity spawned) {
        if(original instanceof Villager){
            ((Villager) spawned).setProfession(((Villager) original).getProfession());
        }
    }

    @Override
    public String getConfigPath() {
        return "compare.villager-profession";
    }
}
