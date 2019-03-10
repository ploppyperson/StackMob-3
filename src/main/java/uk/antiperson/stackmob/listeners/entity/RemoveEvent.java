package uk.antiperson.stackmob.listeners.entity;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackTools;

import javax.annotation.Nullable;

public class RemoveEvent implements Listener {

    private StackMob sm;
    public RemoveEvent(StackMob sm) {
        this.sm = sm;
        for (World world : Bukkit.getWorlds()) {
            ((CraftWorld) world).getHandle().addIWorldAccess(new CustomWorldAccess());
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        ((CraftWorld) event.getWorld()).getHandle().addIWorldAccess(new CustomWorldAccess());
    }


    class CustomWorldAccess implements IWorldAccess{
        @Override
        public void a(IBlockAccess iBlockAccess, BlockPosition blockPosition, IBlockData iBlockData, IBlockData iBlockData1, int i) {

        }

        @Override
        public void a(BlockPosition blockPosition) {

        }

        @Override
        public void a(int i, int i1, int i2, int i3, int i4, int i5) {

        }

        @Override
        public void a(@Nullable EntityHuman entityHuman, SoundEffect soundEffect, SoundCategory soundCategory, double v, double v1, double v2, float v3, float v4) {

        }

        @Override
        public void a(SoundEffect soundEffect, BlockPosition blockPosition) {

        }

        @Override
        public void a(ParticleParam particleParam, boolean b, double v, double v1, double v2, double v3, double v4, double v5) {

        }

        @Override
        public void a(ParticleParam particleParam, boolean b, boolean b1, double v, double v1, double v2, double v3, double v4, double v5) {

        }

        @Override
        public void a(Entity entity) {

        }

        @Override
        public void b(Entity entity) {
            // Should get called for entity death, despawn and chunk unload
            org.bukkit.entity.Entity e = entity.getBukkitEntity();

            if (StackTools.hasValidData(e)) {
                StackTools.removeSize(e);
            } else if (StackTools.isWaiting(e)) {
                StackTools.removeWaiting(e);
            }
        }

        @Override
        public void a(int i, BlockPosition blockPosition, int i1) {

        }

        @Override
        public void a(EntityHuman entityHuman, int i, BlockPosition blockPosition, int i1) {

        }

        @Override
        public void b(int i, BlockPosition blockPosition, int i1) {

        }
    }

}
