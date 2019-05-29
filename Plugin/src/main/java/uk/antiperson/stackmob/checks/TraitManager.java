package uk.antiperson.stackmob.checks;

import org.bukkit.entity.Entity;
import uk.antiperson.stackmob.api.IStackMob;
import uk.antiperson.stackmob.api.checks.ApplicableTrait;
import uk.antiperson.stackmob.api.checks.ComparableTrait;
import uk.antiperson.stackmob.api.checks.ITraitManager;
import uk.antiperson.stackmob.api.checks.SingleTrait;
import uk.antiperson.stackmob.checks.trait.*;

import java.util.HashSet;
import java.util.Set;

public class TraitManager implements ITraitManager {

    private Set<ComparableTrait> comparableTraits = new HashSet<>();
    private IStackMob sm;
    public TraitManager(IStackMob sm){
        this.sm = sm;
    }

    @Override
    public void registerTraits(){
        registerTrait(new AgeableTrait());
        registerTrait(new AnimalsTrait());
        registerTrait(new FireTrait(this));
        registerTrait(new HorseTrait());
        registerTrait(new LeashedTrait());
        registerTrait(new LlamaTrait());
        registerTrait(new ParrotTrait());
        registerTrait(new SheepColorTrait());
        registerTrait(new SheepShearedTrait());
        registerTrait(new SlimeTrait());
        registerTrait(new TameableTrait());
        registerTrait(new ZombieTrait());
        registerTrait(new DrownedTrait());
        registerTrait(new LoveTrait());
    }

    @Override
    public boolean checkTraits(Entity original, Entity nearby){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait.checkTrait(original, nearby)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkTraits(Entity original){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait instanceof SingleTrait){
                SingleTrait singleTrait = (SingleTrait) comparableTrait;
                return singleTrait.checkTrait(original);
            }
        }
        return false;
    }

    @Override
    public void applyTraits(Entity orginal, Entity spawned){
        for(ComparableTrait comparableTrait : comparableTraits){
            if(comparableTrait instanceof ApplicableTrait){
                ApplicableTrait applicableTrait = (ApplicableTrait) comparableTrait;
                applicableTrait.applyTrait(orginal, spawned);
            }
        }
    }

    @Override
    public void registerTrait(ComparableTrait trait){
        if(!sm.getCustomConfig().getBoolean(trait.getConfigPath())){
            return;
        }
        comparableTraits.add(trait);
    }

    @Override
    public IStackMob getStackMob() {
        return sm;
    }
    
}
