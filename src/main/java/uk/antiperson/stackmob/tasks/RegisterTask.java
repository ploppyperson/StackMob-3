package uk.antiperson.stackmob.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.StackMob;

import java.util.List;

public class RegisterTask extends BukkitRunnable {

    private StackMob sm;
    public RegisterTask(StackMob sm){
        this.sm = sm;
    }

    @Override
    public void run(){
        List<World> worlds = Bukkit.getWorlds();
        for(int i = 0; i < worlds.size(); i++){
            int period = (int) Math.round(sm.getCustomConfig().getDouble("task-delay") / worlds.size()) * i;
            new StackTask(sm, worlds.get(i)).runTaskTimer(sm, period, 100);
        }
    }
}
