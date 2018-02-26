package uk.antiperson.stackmob.tools.plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import uk.antiperson.stackmob.StackMob;

import java.lang.reflect.InvocationTargetException;

public class ProtocolSupport{

    private ProtocolManager protocolManager;
    private double xLoc;
    private double yLoc;
    private double zLoc;
    public ProtocolSupport(StackMob sm){
        protocolManager = ProtocolLibrary.getProtocolManager();
        zLoc = sm.config.getCustomConfig().getDouble("tag.show-player-nearby.z");
        yLoc = sm.config.getCustomConfig().getDouble("tag.show-player-nearby.y");
        xLoc = sm.config.getCustomConfig().getDouble("tag.show-player-nearby.x");
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
