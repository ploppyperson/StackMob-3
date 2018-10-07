package uk.antiperson.stackmob.compat.hooks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.compat.Errorable;
import uk.antiperson.stackmob.compat.HookManager;
import uk.antiperson.stackmob.compat.PluginCompat;
import uk.antiperson.stackmob.compat.PluginHook;

import java.lang.reflect.InvocationTargetException;

public class ProtocolLibHook extends PluginHook implements Errorable {

    private ProtocolManager protocolManager;
    private double xLoc;
    private double yLoc;
    private double zLoc;
    public ProtocolLibHook(HookManager hm, StackMob sm){
        super(hm, sm, PluginCompat.PROCOTOLLIB);
    }

    @Override
    public void enable(){
        if(getStackMob().config.getCustomConfig().getBoolean("tag.show-player-nearby.enabled")){
            protocolManager = ProtocolLibrary.getProtocolManager();
            zLoc = getStackMob().config.getCustomConfig().getDouble("tag.show-player-nearby.z");
            yLoc = getStackMob().config.getCustomConfig().getDouble("tag.show-player-nearby.y");
            xLoc = getStackMob().config.getCustomConfig().getDouble("tag.show-player-nearby.x");
            getHookManager().registerHook(PluginCompat.PROCOTOLLIB, this);
        }
    }

    @Override
    public void disable(){
        getStackMob().getLogger().info("ProtocolLib is required for certain features, but it cannot be found!");
        getStackMob().getLogger().info("These feature(s) will not work until ProtocolLib is installed.");
    }

    public void sendUpdatePacket(Player player, Entity entity){
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        // Cloning the packet and getting the entity involved.
        WrappedDataWatcher watcher = new WrappedDataWatcher(entity);
        WrappedDataWatcher.Serializer booleanSerializer = WrappedDataWatcher.Registry.get(Boolean.class);
        // Set if the tag is visible or not.
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, booleanSerializer), false);
        for (Entity e : entity.getNearbyEntities(xLoc, yLoc, zLoc)) {
            if(e.getUniqueId().equals(player.getUniqueId())) {
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, booleanSerializer), true);
                break;
            }
        }
        // Writing the stuff to the packet.
        packet.getEntityModifier(entity.getWorld()).write(0, entity);
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        // Send the new packet.
        try{
            protocolManager.sendServerPacket(player, packet);
        }catch (InvocationTargetException e){
            e.printStackTrace();
        }
    }
}
