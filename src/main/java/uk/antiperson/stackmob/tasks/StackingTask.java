package uk.antiperson.stackmob.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;

public abstract class StackingTask extends BukkitRunnable {

    private double xRadius;
    private double yRadius;
    private double zRadius;
    private StackMob sm;
    public StackingTask(StackMob sm){
        this.sm = sm;
        xRadius = sm.getCustomConfig().getDouble("check-area.x");
        yRadius = sm.getCustomConfig().getDouble("check-area.y");
        zRadius = sm.getCustomConfig().getDouble("check-area.z");
    }

    public double getX() {
        return xRadius;
    }

    public double getY(){
        return yRadius;
    }

    public double getZ(){
        return zRadius;
    }

    public StackMob getStackMob() {
        return sm;
    }
}
