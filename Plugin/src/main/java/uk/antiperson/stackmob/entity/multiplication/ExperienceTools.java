package uk.antiperson.stackmob.entity.multiplication;

import uk.antiperson.stackmob.api.StackMob;
import uk.antiperson.stackmob.api.entity.multiplication.Algorithm;
import uk.antiperson.stackmob.api.entity.multiplication.IExperienceTools;

import java.util.concurrent.ThreadLocalRandom;

import static uk.antiperson.stackmob.api.entity.multiplication.Algorithm.*;

public class ExperienceTools implements IExperienceTools {

    private StackMob sm;
    public ExperienceTools(StackMob sm){
        this.sm = sm;
    }

    @Override
    public int multiplyExperience(int originalExperience, int entityAmount){
        int newExperience;
        if(sm.getCustomConfig().isDouble("multiply-exp.custom-multiplier")) {
            Algorithm algorithm = Algorithm.valueOf(sm.getCustomConfig().getString("multiply-exp.algorithm"));
            if(algorithm == LEGACY) {
                newExperience = (int) Math.round((1.45 + ThreadLocalRandom.current().nextDouble(0.75)) * (entityAmount - 1) * originalExperience);
            }else if(algorithm == MINIMAL) {
                newExperience = (int) Math.round(originalExperience * (0.25 + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount);
            }else if(algorithm == NORMAL) {
                newExperience = (int) Math.round(originalExperience * (0.75 + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount);
            }else if(algorithm == GENEROUS) {
                newExperience = (int) Math.round(originalExperience * (1.25 + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount);
            }else if(algorithm == CUSTOM) {
                double customMultiplier = sm.getCustomConfig().getDouble("multiply-exp.custom-multiplier");
                newExperience = (int) Math.round(originalExperience * (customMultiplier * entityAmount));
            }else{
                double customMultiplier = sm.getCustomConfig().getDouble("multiply-exp.custom-multiplier");
                newExperience = (int) Math.round(originalExperience * (customMultiplier + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount);
            }
        }else{
            newExperience = (int) Math.round(originalExperience * ((0.75 + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount));
        }
        return newExperience;
    }

}

