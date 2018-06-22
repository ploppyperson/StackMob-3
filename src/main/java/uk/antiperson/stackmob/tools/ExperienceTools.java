package uk.antiperson.stackmob.tools;

import uk.antiperson.stackmob.StackMob;

import java.util.concurrent.ThreadLocalRandom;

import static uk.antiperson.stackmob.tools.Algorithm.*;

public class ExperienceTools {

    private StackMob sm;
    public ExperienceTools(StackMob sm){
        this.sm = sm;
    }

    public int multiplyExperience(int originalExperience, int entityAmount){
        int newExperience;
        if(sm.config.getCustomConfig().isDouble("multiply-exp.custom-multiplier")) {
            Algorithm algorithm = Algorithm.valueOf(sm.config.getCustomConfig().getString("multiply-exp.algorithm"));
            if(algorithm == LEGACY) {
                newExperience = (int) Math.round((1.45 + ThreadLocalRandom.current().nextDouble(0.75)) * (entityAmount - 1) * originalExperience);
            }else if(algorithm == MINIMAL) {
                newExperience = (int) Math.round(originalExperience + (originalExperience * ((0.25 + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount)));
            }else if(algorithm == NORMAL) {
                newExperience = (int) Math.round(originalExperience + (originalExperience * ((0.75 + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount)));
            }else if(algorithm == GENEROUS) {
                newExperience = (int) Math.round(originalExperience + (originalExperience * ((1.25 + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount)));
            }else if(algorithm == CUSTOM) {
                double customMultiplier = sm.config.getCustomConfig().getDouble("multiply-exp.custom-multiplier");
                newExperience = (int) Math.round(originalExperience + (originalExperience * (customMultiplier * entityAmount)));
            }else{
                double customMultiplier = sm.config.getCustomConfig().getDouble("multiply-exp.custom-multiplier");
                newExperience = (int) Math.round(originalExperience + (originalExperience * (customMultiplier + ThreadLocalRandom.current().nextDouble(0.5) * entityAmount)));
            }
        }else{
            newExperience = (int) Math.round(originalExperience + (((0.75 + ThreadLocalRandom.current().nextDouble(0.5)) * entityAmount)));
        }
        return newExperience;
    }

}

enum Algorithm{
    LEGACY,
    MINIMAL,
    NORMAL,
    GENEROUS,
    CUSTOM,
    CUSTOM_RANDOM
}
