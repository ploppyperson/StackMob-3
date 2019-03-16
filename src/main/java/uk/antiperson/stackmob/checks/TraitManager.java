package uk.antiperson.stackmob.checks;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.checks.trait.*;

import java.util.HashSet;
import java.util.Set;

public class TraitManager {

    private Set<ComparableTrait> comparableTraits = new HashSet<>();
    private StackMob sm;
    public TraitManager(StackMob sm){
        this.sm = sm;
    }

    public void registerTraits(){
        new AgeableTrait(this);
        new AnimalsTrait(this);
        new FireTrait(this);
        new HorseTrait(this);
        new LeashedTrait(this);
        new LlamaTrait(this);
        new ParrotTrait(this);
        new SheepColorTrait(this);
        new SheepShearedTrait(this);
        new SlimeTrait(this);
        new TameableTrait(this);
        new VillagerTrait(this);
        new ZombieTrait(this);
        new DrownedTrait(this);
        new LoveTrait(this);
    }

    public boolean checkTraits(Entity original, Entity nearby){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait.checkTrait(original, nearby)){
                return true;
            }
        }
        return false;
    }

    public boolean checkTraits(Entity original){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait instanceof SingleTrait){
                SingleTrait singleTrait = (SingleTrait) comparableTrait;
                return singleTrait.checkTrait(original);
            }
        }
        return false;
    }

    public void applyTraits(Entity orginal,  Entity spawned){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait instanceof ApplicableTrait){
                ApplicableTrait applicableTrait = (ApplicableTrait) comparableTrait;
                applicableTrait.applyTrait(orginal, spawned);
            }
        }
    }

    public void registerTrait(ComparableTrait trait){
        comparableTraits.add(trait);
    }

    public StackMob getStackMob() {
        return sm;
    }
}
