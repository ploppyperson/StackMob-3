package uk.antiperson.stackmob.compat.hooks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.api.compat.IHookManager;
import uk.antiperson.stackmob.api.compat.Errorable;
import uk.antiperson.stackmob.api.compat.PluginCompat;
import uk.antiperson.stackmob.compat.PluginHook;

import java.lang.reflect.InvocationTargetException;

public class ProtocolLibHook extends PluginHook implements Errorable {

    private ProtocolManager protocolManager;
    public ProtocolLibHook(IHookManager hm, StackMob sm){
        super(hm, sm, PluginCompat.PROCOTOLLIB);
    }

    @Override
    public void enable(){
        if(getStackMob().getCustomConfig().getBoolean("tag.show-player-nearby.enabled")){
            protocolManager = ProtocolLibrary.getProtocolManager();
            getHookManager().registerHook(PluginCompat.PROCOTOLLIB, this);
        }
    }

    @Override
    public void disable(){
        getStackMob().getLogger().info("ProtocolLib is required for certain features, but it cannot be found!");
        getStackMob().getLogger().info("These feature(s) will not work until ProtocolLib is installed.");
    }

    public void sendPacket(Player player, Entity entity, boolean visible){
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        // Cloning the packet and getting the entity involved.
        WrappedDataWatcher watcher = new WrappedDataWatcher(entity);
        WrappedDataWatcher.Serializer booleanSerializer = WrappedDataWatcher.Registry.get(Boolean.class);
        // Set if the tag is visible or not.
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, booleanSerializer), visible);
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
